package com.hanahakdangserver.classroom.service;


import java.time.LocalDateTime;
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
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.projection.MentorIdOnly;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.redis.RedisBoundHash;
import com.hanahakdangserver.redis.RedisBoundSet;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.CLASSROOM_NOT_USABLE;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.LECTURE_CANCELED;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_ENROLLED;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_FOUND_LECTURE;
import static com.hanahakdangserver.classroom.enums.ClassroomResponseExceptionEnum.NOT_YET_TO_OPEN_CLASSROOM;


@Log4j2
@Service
@Transactional(readOnly = true)
public class ClassroomService {

  private final LectureRepository lectureRepository;
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
      LectureRepository lectureRepository, String classroomMenteeIdSetHashBoundKey) {
    this.lectureRepository = lectureRepository;
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
    LocalDateTime openingTime = lecture.getStartTime().minusMinutes(intervalToOpenLecture);
    return now.isAfter(openingTime) && now.isBefore(lecture.getStartTime());
  }

  public ClassroomStartResponse startClassroom(Long classroomId)
      throws ResponseStatusException {
    Long lectureId = getLectureId(classroomId);

    log.debug("강의 시작, 강의 Id: {}", lectureId);
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(NOT_FOUND_LECTURE::createResponseStatusException);

    log.debug("강의: {}", lecture);
    if (lecture.getIsCanceled()) {
      throw LECTURE_CANCELED.createResponseStatusException();
    }

    if (canLectureBeOpened(lecture)) {
      throw NOT_YET_TO_OPEN_CLASSROOM.createResponseStatusException();
    }

    String classroomIdStr = classroomId.toString();
    String password = UUID.randomUUID().toString();
    classroomPasswordHash.put(classroomIdStr, password);

    return ClassroomStartResponse.builder()
        .lectureId(lecture.getId())
        .password(password)
        .build();
  }

  private boolean isEnrolled(Long classroomId, Long menteeId) {
    RedisBoundSet<Long> classroomMenteeSet = new RedisBoundSet<>(
        classroomMenteeIdSetHashBoundKey + ":" + classroomId, redisTemplate, objectMapper);
    return classroomMenteeSet.exists(menteeId);
  }

  public ClassroomEnterResponse enterClassroom(Long classroomId, Long menteeId)
      throws ResponseStatusException {
    Long lectureId = getLectureId(classroomId);
    log.debug("강의 참가, 강의 Id: {}", lectureId);

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
        .build();
  }
}