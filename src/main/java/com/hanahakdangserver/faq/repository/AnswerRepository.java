package com.hanahakdangserver.faq.repository;

import java.util.List;

import com.hanahakdangserver.faq.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

  /**
   * 질문ID를 기반으로 답변 목록 가져오기
   *
   * @param faqId
   * @return 답변 목록
   */
  List<Answer> findByFaqId(Long faqId);

}
