package com.hanahakdangserver.faq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.faq.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

  /**
   * 강의ID를 기준으로 문의 내용 찾기
   *
   * @param lectureId
   * @return 문의 내용
   */
  @Query("SELECT f FROM Faq f WHERE f.lecture.id = :lectureId")
  List<Faq> findByLectureId(@Param("lectureId") Long lectureId);

}
