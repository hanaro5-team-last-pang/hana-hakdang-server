package com.hanahakdangserver.faq.mapper;

import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FaqMapper {
  
  /**
   * Faq 엔티티를 FaqResponse DTO로 변환
   */
  public static FaqResponse toDto(Faq faq, List<Answer> answers) {
    List<AnswerResponse> answerResponses = answers.stream()
        .map(FaqMapper::toAnswerResponse)
        .collect(Collectors.toList());

    return FaqResponse.builder()
        .id(faq.getId())
        .userName(faq.getUser().getName())
        .imageUrl(faq.getUser().getProfileImageUrl())
        .content(faq.getContent())
        .createdAt(faq.getCreatedAt())
        .answers(answerResponses)
        .build();
  }

  /**
   * Answer 엔티티를 AnswerResponse DTO로 변환
   */
  public static AnswerResponse toAnswerResponse(Answer answer) {
    return AnswerResponse.builder()
        .id(answer.getId())
        .userName(answer.getFaq().getUser().getName())
        .imageUrl(answer.getFaq().getUser().getProfileImageUrl())
        .content(answer.getContent())
        .createdAt(answer.getCreatedAt())
        .build();
  }
}
