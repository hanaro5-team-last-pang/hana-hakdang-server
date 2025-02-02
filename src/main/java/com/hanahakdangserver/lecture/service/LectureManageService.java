package com.hanahakdangserver.lecture.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.common.S3FileProcessor;
import com.hanahakdangserver.common.SnowFlakeGenerator;
import com.hanahakdangserver.lecture.dto.LectureRequest;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.LectureTag;
import com.hanahakdangserver.lecture.repository.CategoryRepository;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.lecture.repository.LectureTagRepository;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.TagRepository;
import com.hanahakdangserver.redis.RedisBoundHash;
import com.hanahakdangserver.redis.RedisString;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.CATEGORY_NOT_FOUND;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.TAG_NOT_FOUND;
import static com.hanahakdangserver.user.enums.UserResponseExceptionEnum.USER_NOT_FOUND;

@Log4j2
@Service
@Transactional(readOnly = true)
public class LectureManageService {

  private final LectureRepository lectureRepository;
  private final CategoryRepository categoryRepository;
  private final ClassroomRepository classroomRepository;
  private final TagRepository tagRepository;
  private final LectureTagRepository lectureTagRepository;
  private final UserRepository userRepository;

  private final SnowFlakeGenerator snowFlakeGenerator; // snowflake 생성기
  private final S3FileProcessor s3FileProcessor;
  private final RedisString redisString;
  private final RedisBoundHash<Long> classroomLectureIdHash;

  @Value("${aws.s3.bucketName}")
  private String bucket;

  public LectureManageService(LectureRepository lectureRepository,
      CategoryRepository categoryRepository, ClassroomRepository classroomRepository,
      TagRepository tagRepository, LectureTagRepository lectureTagRepository,
      UserRepository userRepository, SnowFlakeGenerator snowFlakeGenerator,
      S3FileProcessor s3FileProcessor, RedisString redisString,
      String classroomLectureIdHashBoundKey, RedisTemplate<String, String> redisTemplate,
      ObjectMapper objectMapper) {
    this.lectureRepository = lectureRepository;
    this.categoryRepository = categoryRepository;
    this.classroomRepository = classroomRepository;
    this.tagRepository = tagRepository;
    this.lectureTagRepository = lectureTagRepository;
    this.userRepository = userRepository;
    this.snowFlakeGenerator = snowFlakeGenerator;
    this.s3FileProcessor = s3FileProcessor;
    this.redisString = redisString;
    this.classroomLectureIdHash = new RedisBoundHash<>(classroomLectureIdHashBoundKey,
        redisTemplate, objectMapper);
  }

  /**
   * 강의 객체와 강의실 객체를 생성한 후 DB에 저장합니다.
   *
   * @param lectureRequest 강의 생성 Request JSON
   */
  @Transactional
  public void registerNewLecture(Long userId, MultipartFile imageFile,
      LectureRequest lectureRequest)
      throws IOException {

    User mentor = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    Long uniqueId = snowFlakeGenerator.nextId(); // 강의실에 대해 전역적으로 고유한 Id 생성

    Classroom classroom = classroomRepository.save(
        Classroom.builder().id(uniqueId).build()
    ); // 연관된 강의실 생성 -> DB에 저장

    Category category = categoryRepository.findByName(lectureRequest.getCategory().getDescription())
        .orElseThrow(CATEGORY_NOT_FOUND::createResponseStatusException);

    // TODO : 이미지 파일이 없을 경우 예외 처리 필요
    String thumbnailUrl = s3FileProcessor.uploadImageFileToS3(bucket, imageFile);

    Lecture lecture = lectureRepository.save(
        Lecture.builder()
            .classroom(classroom)
            .mentor(mentor)
            .category(category)
            .title(lectureRequest.getTitle())
            .startTime(lectureRequest.getStartTime())
            .endTime(lectureRequest.getEndTime())
            .maxParticipants(lectureRequest.getMaxParticipants())
            .description(lectureRequest.getDescription())
            .thumbnailUrl(thumbnailUrl)
            .build()
    );

    // LECTURE_TAG에도 저장
    lectureRequest.getTags().forEach(tagId -> {
      Tag tag = tagRepository.findById(tagId)
          .orElseThrow(TAG_NOT_FOUND::createResponseStatusException);
      lectureTagRepository.save(
          LectureTag.builder()
              .lecture(lecture)
              .tag(tag)
              .build()
      );
    });

    // 동시성 처리를 위해 Redis에 현재 수강신청한 인원 관리
    putLectureIdString(lecture.getId(), lecture.getStartTime());

    // { 강의실 : 강의 } 관리를 위해 레디스의 classroomLectureIdHash에 저장
    putLectureIdHash(uniqueId, lecture.getId());

    // TODO : 강의 취소 시에는 classroomLectureIdHash에서 제거
  }

  /**
   * 레디스에 { lecture:lectureId : "0" } 저장
   *
   * @param lectureId key 생성을 위한 강의 Id
   * @param startTime TTL 계산을 위한 강의 시작시간
   */
  private void putLectureIdString(Long lectureId, LocalDateTime startTime) {
    String lectureKey = "lecture:" + lectureId; // lecture id를 사용하여 Redis 키 생성
    // TTL 계산: startTime에서 현재 시간 차이 + 1시간
    Instant now = Instant.now();
    Instant startTimeInstant = startTime
        .atZone(ZoneId.of("Asia/Seoul"))
        .toInstant();

    Duration ttlDuration = Duration.between(now, startTimeInstant);
    ttlDuration = ttlDuration.plusHours(1);

    redisString.putWithTTL(lectureKey, "0", ttlDuration); // 수강신청한 인원 수 0으로 초기화
  }

  /**
   * 레디스 classroomLectureIdHash에 { classroomId : lectureId } 해시 저장
   *
   * @param classroomId key
   * @param lectureId   value
   */
  private void putLectureIdHash(Long classroomId, Long lectureId) {
    classroomLectureIdHash.put(classroomId.toString(), lectureId); // key는 String이기 때문
  }

}
