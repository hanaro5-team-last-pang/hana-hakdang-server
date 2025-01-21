package com.hanahakdangserver.faq.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
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

  @Transactional
  public FaqResponse createFaq(Long lectureId, FaqRequest request, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);

    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);

    Faq faq = Faq.builder()
        .lecture(lecture)
        .content(request.getContent())
        .build();

    Faq savedFaq = faqRepository.save(faq);
    return FaqMapper.toDto(savedFaq, List.of());
  }

  public List<FaqResponse> getFaqsByLectureId(Long lectureId) {
    List<Faq> faqs = faqRepository.findByLectureId(lectureId);

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
