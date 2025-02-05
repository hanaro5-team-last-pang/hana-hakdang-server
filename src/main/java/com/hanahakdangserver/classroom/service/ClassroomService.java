package com.hanahakdangserver.classroom.service;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanahakdangserver.classroom.dto.ClassroomEnterResponse;
import com.hanahakdangserver.classroom.dto.ClassroomStartResponse;
import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.classroom.repository.ClassroomRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.projection.MentorIdOnly;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.notification.service.NotificationService;
import com.hanahakdangserver.redis.RedisBoundHash;
import com.hanahakdangserver.redis.RedisBoundSet;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.CLASSROOM_NOT_USABLE;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.LECTURE_CANCELED;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_ENROLLED;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_FOUND_CLASSROOM;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_FOUND_LECTURE;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_YET_TO_OPEN_CLASSROOM;


@Log4j2
@Service
@Transactional(readOnly = true)
public class ClassroomService {

  private final NotificationService notificationService;
  private final LectureRepository lectureRepository;
  private final ClassroomRepository classroomRepository;
  private final RedisBoundHash<String> classroomPasswordHash;
  private final RedisBoundHash<Long> classroomLectureIdHash;
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;
  private final String classroomMenteeIdSetHashBoundKey;

  @Value("${classroom.interval-to-open-lecture}")
  private long intervalToOpenLecture;

  public ClassroomService(
      String classroomEntranceHashBoundKey,
      String classroomLectureIdHashBoundKey,
      ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate,
      NotificationService notificationService,
      LectureRepository lectureRepository, ClassroomRepository classroomRepository,
      String classroomMenteeIdSetHashBoundKey) {
    this.notificationService = notificationService;
    this.lectureRepository = lectureRepository;
    this.classroomRepository = classroomRepository;
    this.classroomPasswordHash = new RedisBoundHash<>(classroomEntranceHashBoundKey, redisTemplate,
        objectMapper);
    this.classroomLectureIdHash = new RedisBoundHash<>(classroomLectureIdHashBoundKey,
        redisTemplate, objectMapper);
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
    this.classroomMenteeIdSetHashBoundKey = classroomMenteeIdSetHashBoundKey;
  }

  private Long getLectureId(Long classroomId) throws ResponseStatusException {
    return classroomLectureIdHash.get(classroomId.toString(), Long.class)
        .orElseThrow(CLASSROOM_NOT_USABLE::createResponseStatusException);
  }

  private boolean canLectureBeOpened(Lecture lecture) {
    LocalDateTime now = LocalDateTime.now();
    // 15분 전
    LocalDateTime openingTimeStart = lecture.getStartTime()
        .minusMinutes(intervalToOpenLecture);
    // 15분 후
    LocalDateTime openingTimeEnd = lecture.getStartTime()
        .plusMinutes(intervalToOpenLecture);

    log.info("시작 가능 시간: {} ~ {}", openingTimeStart, openingTimeEnd);
    return now.isAfter(openingTimeStart) && now.isBefore(openingTimeEnd);
  }

  @Transactional
  public ClassroomStartResponse startClassroom(Long classroomId)
      throws ResponseStatusException {
    Long lectureId = getLectureId(classroomId);

    log.debug("강의 시작, 강의 Id: {}", lectureId);
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(NOT_FOUND_LECTURE::createResponseStatusException);

    if (lecture.getIsCanceled()) {
      throw LECTURE_CANCELED.createResponseStatusException();
    }

    if (!canLectureBeOpened(lecture)) {
      throw NOT_YET_TO_OPEN_CLASSROOM.createResponseStatusException();
    }

    lecture.updateIsDone(true);
    lecture.updateStartTime(LocalDateTime.now());

    String classroomIdStr = classroomId.toString();
    String password = UUID.randomUUID().toString();
    classroomPasswordHash.put(classroomIdStr, password);

    sendStartNotification(classroomId, lectureId); // 강의 시작 알림 전송

    return ClassroomStartResponse.builder()
        .lectureId(lecture.getId())
        .password(password)
        .lectureTitle(lecture.getTitle())
        .build();
  }

  private void sendStartNotification(Long classroomId, Long lectureId) {
    Set<Long> mentees = getMentees(classroomId);
    mentees.forEach(menteeId -> {
      notificationService.noticeLectureStarted(menteeId, lectureId);
    });
  }

  private RedisBoundSet<Long> getRedisMenteeSet(Long classroomId) {
    return new RedisBoundSet<>(
        classroomMenteeIdSetHashBoundKey + ":" + classroomId, redisTemplate, objectMapper);
  }

  private Set<Long> getMentees(Long classroomId) {
    RedisBoundSet<Long> classroomMenteeSet = getRedisMenteeSet(classroomId);
    return classroomMenteeSet.getAll(Long.class);
  }

  private boolean isEnrolled(Long classroomId, Long menteeId) {
    RedisBoundSet<Long> classroomMenteeSet = getRedisMenteeSet(classroomId);
    return classroomMenteeSet.exists(menteeId);
  }

  public ClassroomEnterResponse enterClassroom(Long classroomId, Long menteeId)
      throws ResponseStatusException {
    Long lectureId = getLectureId(classroomId);
    log.debug("강의 참가, 강의 Id: {}", lectureId);

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(NOT_FOUND_LECTURE::createResponseStatusException);

    if (!isEnrolled(classroomId, menteeId)) {
      throw NOT_ENROLLED.createResponseStatusException();
    }

    String classroomPwd = classroomPasswordHash.get(classroomId.toString(), String.class)
        .orElseThrow(CLASSROOM_NOT_USABLE::createResponseStatusException);

    MentorIdOnly mentorIdOnly = lectureRepository
        .findMentorIdById(lectureId)
        .orElseThrow(NOT_FOUND_LECTURE::createResponseStatusException);

    return ClassroomEnterResponse.builder()
        .password(classroomPwd)
        .mentorId(mentorIdOnly.getMentorId())
        .lectureId(lectureId)
        .lectureTitle(lecture.getTitle())
        .build();
  }

  @Transactional
  public void terminateClassroom(Long classroomId) throws ResponseStatusException {

    Long lectureId = getLectureId(classroomId);
    log.debug("강의 종료 시도, 강의 Id: {}", lectureId);

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(NOT_FOUND_LECTURE::createResponseStatusException);

    Classroom classroom = classroomRepository.findById(classroomId)
        .orElseThrow(NOT_FOUND_CLASSROOM::createResponseStatusException);

    lecture.updateEndTime(LocalDateTime.now()); // 강의 endTime 업데이트
    classroom.updateIsUsed(false);

    // 레디스에서 관련 데이터 삭제
    classroomLectureIdHash.delete(classroomId.toString());
    classroomPasswordHash.delete(classroomId.toString());
    redisTemplate.delete(classroomMenteeIdSetHashBoundKey + ":" + classroomId);
  }
}