package com.hanahakdangserver.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.enrollment.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
//  // 강의 등록 사용자 목록 조회
//  @Query("SELECT e.user FROM Enrollment e WHERE e.lecture.id = :lectureId")
//  List<User> findUsersByLectureId(@Param("lectureId") Long lectureId);
//
//  // 특정 강의에 대한 수강신청 여부 확인
//  @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.user.id = :userId AND e.lecture.id = :lectureId")
//  boolean existsByUserIdAndLectureId(@Param("userId") Long userId,
//      @Param("lectureId") Long lectureId);
//
//  // 수강 신청한 사용자 수 조회
//  @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.lecture.id = :lectureId AND e.isCanceled = false")
//  Long countEnrollmentsByLectureId(@Param("lectureId") Long lectureId);
}
