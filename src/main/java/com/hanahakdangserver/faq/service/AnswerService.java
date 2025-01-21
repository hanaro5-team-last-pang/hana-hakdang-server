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
import com.hanahakdangserver.user.entity.User;
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
   * @param request
   * @return 등록된 답변 내용
   */
  @Transactional
  public AnswerResponse createAnswer(AnswerRequest request) {
    // FAQ 엔티티 조회
    Faq faq = faqRepository.findById(request.getFaqId())
        .orElseThrow(FAQ_NOT_FOUND::createResponseStatusException);

    // 사용자 엔티티 조회
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    // Answer 엔티티 생성
    Answer answer = Answer.builder()
        .faq(faq)
        .content(request.getContent())
        .build();

    // Answer 저장
    Answer savedAnswer = answerRepository.save(answer);

    // Mapper를 통해 DTO 반환
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
