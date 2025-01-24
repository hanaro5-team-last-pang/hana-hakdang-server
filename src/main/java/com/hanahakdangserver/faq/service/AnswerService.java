package com.hanahakdangserver.faq.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.faq.dto.AnswerRequest;
import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.mapper.AnswerMapper;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;

import static com.hanahakdangserver.faq.enums.FaqResponseExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

  private final AnswerRepository answerRepository;
  private final FaqRepository faqRepository;

  /**
   * 답변 등록
   *
   * @param request
   * @@return 등록된 답변 내용
   */
  @Transactional
  public AnswerResponse createAnswer(AnswerRequest request) {
    Faq faq = faqRepository.findById(request.getFaqId())
        .orElseThrow(FAQ_NOT_FOUND::createResponseStatusException);

    Answer answer = Answer.builder()
        .faq(faq)
        .content(request.getContent())
        .build();

    Answer savedAnswer = answerRepository.save(answer);

    return AnswerMapper.toDto(savedAnswer);
  }

  /**
   * 답변 삭제
   *
   * @param answerId
   */
  @Transactional
  public void deleteAnswer(Long answerId) {
    Answer answer = answerRepository.findById(answerId)
        .orElseThrow(ANSWER_NOT_FOUND::createResponseStatusException);

    answerRepository.delete(answer);
  }
}
