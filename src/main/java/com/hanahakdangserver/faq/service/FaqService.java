package com.hanahakdangserver.faq.service;

import com.hanahakdangserver.faq.dto.*;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.mapper.FaqMapper;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

  private final FaqRepository faqRepository;
  private final AnswerRepository answerRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;


  /**
   * 문의 등록
   *
   * @param lectureId
   * @param request
   * @return 등록된 문의 내용
   */
  @Transactional
  public FaqResponse createFaq(Long lectureId, FaqRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다."));

    Faq faq = Faq.builder()
        .user(user)
        .lecture(lecture)
        .content(request.getContent())
        .build();

    Faq savedFaq = faqRepository.save(faq);
    return FaqMapper.toDto(savedFaq, List.of());
  }


  /**
   * 특정 강의에 대한 문의 조회
   *
   * @param lectureId
   * @return 문의 내용 전체 (답변 포함)
   */
  public List<FaqResponse> getFaqsByLectureId(Long lectureId) {
    List<Faq> faqs = faqRepository.findByLectureId(lectureId);

    return faqs.stream()
        .map(faq -> {
          List<Answer> answers = answerRepository.findByFaqId(faq.getId());
          return FaqMapper.toDto(faq, answers);
        })
        .collect(Collectors.toList());
  }


  /**
   * 문의 삭제
   *
   * @param faqId
   */
  @Transactional
  public void deleteFaq(Long faqId) {
    Faq faq = faqRepository.findById(faqId)
        .orElseThrow(() -> new EntityNotFoundException("문의가 존재하지 않습니다."));
    faqRepository.delete(faq);
  }
}
