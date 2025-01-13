package com.hanahakdangserver.faq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.faq.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

//  // 특정 강의와 관련된 문의 조회
//  @Query("SELECT f FROM Faq f WHERE f.lecture.id = :lectureId")
//  List<Faq> findByLectureId(@Param("lectureId") Long lectureId);
//
//  // 특정 사용자가 작성한 문의 조회
//  @Query("SELECT f FROM Faq f WHERE f.user.id = :userId")
//  List<Faq> findByUserId(@Param("userId") Long userId);
//
//  // 문의 ID로 문의 조회
//  @Query("SELECT f FROM Faq f WHERE f.id = :faqId")
//  Faq findByFaqId(@Param("faqId") Long faqId);
}
