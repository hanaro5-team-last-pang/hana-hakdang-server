package com.hanahakdangserver.lecture.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.classroom.utils.SnowFlakeGenerator;
import com.hanahakdangserver.lecture.dto.LectureRequest;
import com.hanahakdangserver.lecture.entity.Category;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.entity.LectureTag;
import com.hanahakdangserver.lecture.repository.CategoryRepository;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.lecture.repository.LectureTagRepository;
import com.hanahakdangserver.product.entity.Tag;
import com.hanahakdangserver.product.repository.TagRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.CATEGORY_NOT_FOUND;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.TAG_NOT_FOUND;
import static com.hanahakdangserver.user.enums.UserResponseExceptionEnum.USER_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LectureService {

  private final LectureRepository lectureRepository;
  private final CategoryRepository categoryRepository;
  private final ClassroomRepository classroomRepository;
  private final TagRepository tagRepository;
  private final LectureTagRepository lectureTagRepository;
  private final UserRepository userRepository;

  private final SnowFlakeGenerator snowFlakeGenerator; // snowflake 생성기
  private final S3AsyncClient s3AsyncClient;

  @Value("${aws.s3.bucketName}")
  private String bucket;

  /**
   * 강의 객체와 강의실 객체를 생성한 후 DB에 저장합니다.
   *
   * @param lectureRequest 강의 생성 Request JSON
   */
  @Transactional
  public void registerNewLecture(String userEmail, MultipartFile imageFile,
      LectureRequest lectureRequest)
      throws IOException {

    User mentor = userRepository.findByEmail(userEmail)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    Long uniqueId = snowFlakeGenerator.nextId(); // 강의실에 대해 전역적으로 고유한 Id 생성

    Classroom classroom = classroomRepository.save(
        Classroom.builder().id(uniqueId).build()
    ); // 연관된 강의실 생성 -> DB에 저장

    Category category = categoryRepository.findByName(lectureRequest.getCategory().getDescription())
        .orElseThrow(CATEGORY_NOT_FOUND::createResponseStatusException);

    // TODO : 이미지 파일이 없을 경우 예외 처리 필요
    String thumbnailUrl = uploadImageFileToS3(imageFile);

    Lecture lecture = lectureRepository.save(
        Lecture.builder()
            .classroom(classroom)
            .mentor(mentor)
            .category(category)
            .title(lectureRequest.getTitle())
            .startTime(lectureRequest.getStartTime())
            .duration(lectureRequest.getDuration())
            .maxParticipants(lectureRequest.getMaxParticipants())
            .description(lectureRequest.getDescription())
            .thumbnailUrl(thumbnailUrl)
            .build()
    );

    // LECTURE_TAG에도 저장
    for (Long tagId : lectureRequest.getTags()) {
      Tag tag = tagRepository.findById(tagId)
          .orElseThrow(TAG_NOT_FOUND::createResponseStatusException);
      lectureTagRepository.save(
          LectureTag.builder()
              .lecture(lecture)
              .tag(tag)
              .build()
      );
    }
  }

  /**
   * 이미지를 S3 버킷에 업로드 하고 url 주소를 받아오는 메서드
   *
   * @param imageFile 업로드할 이미지 파일
   * @return S3에 업로드된 url 반환
   * @throws IOException
   */
  private String uploadImageFileToS3(MultipartFile imageFile) throws IOException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    try (InputStream inputStream = imageFile.getInputStream()) {
      String originalFilename = imageFile.getOriginalFilename();
      String uuid = UUID.randomUUID().toString();
      String imageFileName = uuid + originalFilename;

      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(imageFileName)
          .contentType(imageFile.getContentType())
          .contentLength(imageFile.getSize())
          .build();

      // 파일 업로드 (비동기 처리)
      CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(
          putObjectRequest,
          AsyncRequestBody.fromInputStream(
              inputStream,
              imageFile.getSize(),
              executorService
          )
      );

      // 비동기 결과를 기다리고 응답 확인
      PutObjectResponse response = future.join();

      // 업로드 성공 여부 확인
      if (response.sdkHttpResponse().isSuccessful()) {
        // 업로드된 파일의 URL 가져오기
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucket,
            Region.AP_NORTHEAST_2.toString().toLowerCase(),
            imageFileName);
        return imageUrl;

      } else {
        throw new RuntimeException(
            "S3 업로드 실패: " + response.sdkHttpResponse().statusText().orElse("UNKNOWN ERROR"));
      }

    } catch (IOException e) {
      log.warn("파일 업로드 실패: {}", e.getMessage());
      throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
    }
  }
}
