package com.hanahakdangserver.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
//  // 특정 강의 리뷰 조회
//  @Query("SELECT r FROM Review r WHERE r.lecture.id = :lectureId")
//  List<Review> findByLectureId(@Param("lectureId") Long lectureId);
//
//  // 특정 사용자가 작성한 리뷰 조회
//  @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
//  List<Review> findByUserId(@Param("userId") Long userId);
//
//  // 강의 평균 평점
//  @Query("SELECT AVG(r.score) FROM Review r WHERE r.lecture.id = :lectureId")
//  Double findAverageScoreByLectureId(@Param("lectureId") Long lectureId);
//
//  // 멘토의 강의 리뷰 전체 조회
//  @Query("SELECT r FROM Review r WHERE r.lecture.mentor.id = :mentorId")
//  List<Review> findReviewsByMentorId(@Param("mentorId") Long mentorId);
}
