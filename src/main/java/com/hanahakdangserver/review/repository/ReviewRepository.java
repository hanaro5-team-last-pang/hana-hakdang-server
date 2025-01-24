package com.hanahakdangserver.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.hanahakdangserver.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * 강의ID 기반 리뷰 조회 -> 페이징 처리 O
   *
   * @param lectureId 강의ID
   * @return 강의에 대한 리뷰 목록
   */
  @Query("SELECT r FROM Review r WHERE r.lecture.id = :lectureId")
  Page<Review> findByLectureId(@Param("lectureId") Long lectureId, Pageable pageable);


  /**
   * 강의ID 기반 리뷰 조회 -> 페이징 처리 X
   *
   * @param lectureId 강의 아이디
   * @return 강의에 대한 리뷰 목록
   */
  @Query("SELECT r FROM Review r WHERE r.lecture.id = :lectureId")
  List<Review> findAllByLectureId(@Param("lectureId") Long lectureId);
}
