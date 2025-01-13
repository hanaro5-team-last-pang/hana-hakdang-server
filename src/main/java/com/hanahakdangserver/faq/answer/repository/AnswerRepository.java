package com.hanahakdangserver.faq.answer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanahakdangserver.faq.answer.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

//  // 특정 강의와 관련된 문의
//  @Query("SELECT a FROM Answer a WHERE a.faq.id = :faqId")
//  List<Answer> findByFaqId(@Param("faqId") Long faqId);
//
//  // 답변 ID로 답변 조회
//  @Query("SELECT a FROM Answer a WHERE a.id = :answerId")
//  Answer findByAnswerId(@Param("answerId") Long answerId);

}
