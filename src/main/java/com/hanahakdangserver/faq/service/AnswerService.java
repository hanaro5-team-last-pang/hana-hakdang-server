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
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.user.repository.UserRepository;

import static com.hanahakdangserver.faq.enums.FaqResponseExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

  private final AnswerRepository answerRepository;
  private final FaqRepository faqRepository;
  private final UserRepository userRepository;
  
  /**
   * 답변 등록
   *
   * @param faqId
   * @param request
   * @param userId
   * @return 등록된 답변 내용
   */
  @Transactional
  public AnswerResponse createAnswer(Long faqId, AnswerRequest request, Long userId) {
    Faq faq = faqRepository.findById(faqId)
        .orElseThrow(FAQ_NOT_FOUND::createResponseStatusException);

    Lecture lecture = faq.getLecture();

    // 강의 개설자인지 검증
    if (lecture.getMentor() == null || !lecture.getMentor().getId().equals(userId)) {
      throw FAQ_PERMISSION_DENIED.createResponseStatusException();
    }

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
   * @param faqId
   * @param answerId
   * @param userId
   */
  @Transactional
  public void deleteAnswer(Long faqId, Long answerId, Long userId) {

    Faq faq = faqRepository.findById(faqId)
        .orElseThrow(FAQ_NOT_FOUND::createResponseStatusException);

    Lecture lecture = faq.getLecture();

    // 강의 개설자인지 검증
    if (lecture.getMentor() == null || !lecture.getMentor().getId().equals(userId)) {
      throw FAQ_PERMISSION_DENIED.createResponseStatusException();
    }

    Answer answer = answerRepository.findById(answerId)
        .orElseThrow(ANSWER_NOT_FOUND::createResponseStatusException);

    answerRepository.delete(answer);
  }
}
