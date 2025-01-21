package com.hanahakdangserver.lecture.enrollment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>,
    EnrollmentRepositoryCustom {

  Optional<Enrollment> findByUserIdAndLectureId(Long studentId, Long lectureId);
//  // 강의 등록 사용자 목록 조회
//  @Query("SELECT e.user FROM Enrollment e WHERE e.lecture.id = :lectureId")
//  List<User> findUsersByLectureId(@Param("lectureId") Long lectureId);
//
//  // 수강 신청한 사용자 수 조회
//  @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.lecture.id = :lectureId AND e.isCanceled = false")
//  Long countEnrollmentsByLectureId(@Param("lectureId") Long lectureId);
}
