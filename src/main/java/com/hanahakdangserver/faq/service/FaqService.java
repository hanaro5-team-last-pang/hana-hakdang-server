package com.hanahakdangserver.faq.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.faq.dto.FaqRequest;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.faq.mapper.FaqMapper;
import com.hanahakdangserver.faq.repository.AnswerRepository;
import com.hanahakdangserver.faq.repository.FaqRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

import static com.hanahakdangserver.faq.enums.FaqResponseExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

  private final FaqRepository faqRepository;
  private final AnswerRepository answerRepository;
  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  /**
   * FAQ + Answer를 합쳐서 페이지네이션 처리
   */
  public List<Object> getPaginatedFaqs(Long lectureId, Pageable pageable) {
    Page<Faq> faqPage = faqRepository.findByLectureId(lectureId, pageable);
    List<Object> combinedList = new ArrayList<>();

    for (Faq faq : faqPage.getContent()) {
      // FAQ 추가
      List<Answer> answers = answerRepository.findByFaqId(faq.getId());
      combinedList.add(FaqMapper.toDto(faq, answers));

      // 해당 FAQ의 답변 추가
      answers.stream()
          .map(FaqMapper::toAnswerResponse)
          .forEach(combinedList::add);
    }

    return combinedList;
  }
  
  @Transactional
  public FaqResponse createFaq(Long lectureId, FaqRequest request, Long userId) {
    // 사용자 조회
    User user = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    // 강의 조회
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    // Faq 엔티티 생성 및 저장
    Faq faq = Faq.builder()
        .lecture(lecture)
        .user(user) // user 설정
        .content(request.getContent())
        .build();

    Faq savedFaq = faqRepository.save(faq);

    // DTO 변환 후 반환
    return FaqMapper.toDto(savedFaq, List.of());
  }


  public List<FaqResponse> getFaqsByLectureId(Long lectureId, Pageable pageable) {
    Page<Faq> faqs = faqRepository.findByLectureId(lectureId, pageable);

    return faqs.stream()
        .map(faq -> {
          List<Answer> answers = answerRepository.findByFaqId(faq.getId());
          return FaqMapper.toDto(faq, answers);
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteFaq(Long faqId, Long userId) {
    Faq faq = faqRepository.findById(faqId)
        .orElseThrow(FAQ_NOT_FOUND::createResponseStatusException);

    if (!faq.getUser().getId().equals(userId)) {
      throw UNAUTHORIZED_ACTION.createResponseStatusException();
    }

    faqRepository.delete(faq);
  }
}
