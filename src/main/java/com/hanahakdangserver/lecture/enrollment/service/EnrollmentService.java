package com.hanahakdangserver.lecture.enrollment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;
import com.hanahakdangserver.lecture.enrollment.repository.EnrollmentRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.redis.RedisString;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseExceptionEnum.ALREADY_ENROLLED;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseExceptionEnum.ENROLL_IS_NOT_ALLOWED;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseExceptionEnum.INVALID_ENROLLMENT;
import static com.hanahakdangserver.lecture.enrollment.enums.EnrollmentResponseExceptionEnum.NOT_ENROLLED;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.LECTURE_NOT_FOUND;
import static com.hanahakdangserver.user.enums.UserResponseExceptionEnum.USER_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EnrollmentService {

  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;
  private final EnrollmentRepository enrollmentRepository;

  private final RedisString redisString;

  /**
   * 수강 신청 가능한 강의에 대해 수강신청
   *
   * @param userId    멘티인 유저의 id
   * @param lectureId 수강신청 하려는 강의의 id
   */
  @Transactional
  public void enrollLecture(Long userId, Long lectureId) {

    // 이미 유저가 수강신청한 강의인지 확인
    if (enrollmentRepository.isAlreadyEnrolled(userId, lectureId)) {
      throw ALREADY_ENROLLED.createResponseStatusException();
    }

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);
    User mentee = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    // 수강신청 가능한 상태의 강의인지 확인
    if (lecture.getIsFull() || lecture.getIsCanceled() || lecture.getStartTime().isBefore(now())) {
      throw ENROLL_IS_NOT_ALLOWED.createResponseStatusException();
    }

    Long currCount = redisString.increment("lecture:" + lectureId, 1);
    log.info("## 강의명: {}, 현재 수강신청 인원: {}명", lecture.getTitle(), currCount);

    // 아직 인원이 다 차지 않아 수강신청 가능한 강의
    if (currCount <= Long.valueOf(lecture.getMaxParticipants())) {

      // 기존에 해당 강의에 대해 수강신청 취소 했던 내역이 있는지 확인
      Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
          .orElse(null);

      if (enrollment == null) {
        // 기존에 해당 강의에 대한 수강신청 내역이 전혀 없는 경우 DB에 저장
        enrollmentRepository.save(
            Enrollment.builder()
                .user(mentee)
                .lecture(lecture)
                .build()
        );
      } else {
        enrollment.updateIsCanceled(false);
      }

      if (currCount.equals(Long.valueOf(lecture.getMaxParticipants()))) {
        // 인원이 가득 차 모집완료로 전환
        lecture.updateIsFull(true);
      }

    } else {
      redisString.decrement("lecture:" + lectureId, 1);
      throw ENROLL_IS_NOT_ALLOWED.createResponseStatusException();
    }
  }

  /**
   * 수강신청 취소
   *
   * @param userId       멘티인 유저의 Id
   * @param enrollmentId 수강신청 취소하고자 하는 수강신청 엔티티 Id
   */
  @Transactional
  public void withdrawEnrollment(Long userId, Long enrollmentId) {

    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
        .orElseThrow(INVALID_ENROLLMENT::createResponseStatusException);

    // 유저가 수강신청한 강의인지 확인
    if (!enrollmentRepository.isAlreadyEnrolled(userId, enrollment.getLecture().getId())) {
      throw NOT_ENROLLED.createResponseStatusException();
    }

    // 아직 수강신청 취소하지 않은 경우; isCanceled == false
    if (!enrollment.getIsCanceled()) {
      enrollment.updateIsCanceled(true);

      // 수강신청한 인원이 한 명 줄게 되므로 Redis에서 관리하던 수강신청 인원 수도 변경
      redisString.decrement("lecture:" + enrollment.getLecture().getId(), 1);

      // 수강신청 인원이 가득 차있던 강의면 다시 수강신청 받을 수 있도록 상태 변경
      if (enrollment.getLecture().getIsFull()) {
        enrollment.getLecture().updateIsFull(false);
      }
    }
  }
}
