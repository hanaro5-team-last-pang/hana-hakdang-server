package com.hanahakdangserver.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.hanahakdangserver.review.entity.Review;
import com.hanahakdangserver.review.projection.ReviewProjection;

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


  /**
   * 강의 ID 기준 리뷰를 조회하고 페이징 처리된 결과 반환
   *
   * @param lectureId 강의 아이디
   * @param pageable  페이징 처리를 위한 객체
   * @return 리뷰 정보 (ReviewProjection 인터페이스를 구현한 DTO)
   */

  @Query("""
          SELECT 
              AVG(r.score) AS averageScore,
              COUNT(r.id) AS totalCount,
              r.score AS score,
              COUNT(r.score) AS count,
              r.id AS id,
              r.lecture.id AS lectureId,
              r.user.id AS userId,
              r.user.name AS userName,
              r.user.profileImageUrl AS imageUrl,
              r.lecture.title AS lectureTitle,
              r.content AS content,
              r.createdAt AS createdAt
          FROM Review r
          WHERE r.lecture.id = :lectureId
          GROUP BY r.id, r.score, r.user.id, r.user.name, r.user.profileImageUrl, r.lecture.title, r.content, r.createdAt
      """)
  Page<ReviewProjection> findReviewsByLectureId(@Param("lectureId") Long lectureId,
      Pageable pageable);


}
