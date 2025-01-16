package com.hanahakdangserver.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * 강의ID 기반 리뷰 조회
   *
   * @param lectureId
   * @return 강의에 대한 리뷰 목록
   */
  @Query("SELECT r FROM Review r WHERE r.lecture.id = :lectureId")
  List<Review> findByLectureId(@Param("lectureId") Long lectureId);


  /**
   * 멘토에 대한 전체 리뷰
   *
   * @param mentorId
   * @return 리뷰 목록
   */
  @Query("SELECT r FROM Review r WHERE r.lecture.mentor.id = :mentorId")
  List<Review> findReviewsByMentorId(@Param("mentorId") Long mentorId);

}
