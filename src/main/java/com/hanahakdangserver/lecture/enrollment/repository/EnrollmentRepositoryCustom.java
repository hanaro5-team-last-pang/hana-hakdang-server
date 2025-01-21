package com.hanahakdangserver.lecture.enrollment.repository;

import java.util.List;

import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;

public interface EnrollmentRepositoryCustom {

  /**
   * 특정 유저가 특정 강의에 대해 이미 수강신청 했는지 여부를 반환
   *
   * @param userId    수강신청 여부를 확인하고 싶은 유저 id
   * @param lectureId 수강신청 여부를 확인하고 싶은 강의 id
   * @return 이미 수강신청했으면 true, 아니면 false
   */
  Boolean isAlreadyEnrolled(Long userId, Long lectureId);

  /**
   * 유저가 특정 강의에 대해 기존에 수강신청 했던 Enrollment 반환
   *
   * @param userId    유저 Id
   * @param lectureId 강의 Id
   * @return 기존 수강신청 객체가 담긴 List, 빈 리스트면 결과가 없다는 의미
   */
  List<Enrollment> getExistingEnrollment(Long userId, Long lectureId);
}
