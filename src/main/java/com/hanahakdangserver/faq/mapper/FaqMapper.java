package com.hanahakdangserver.faq.mapper;

import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.dto.FaqResponse;
import com.hanahakdangserver.faq.entity.Answer;
import com.hanahakdangserver.faq.entity.Faq;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FaqMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy년 MM월 dd일");

  /**
   * Faq 엔티티를 FaqResponse DTO로 변환
   */
  public static FaqResponse toDto(Faq faq, List<Answer> answers) {
    List<AnswerResponse> answerResponses = answers.stream()
        .map(FaqMapper::toAnswerResponse)
        .collect(Collectors.toList());

    return FaqResponse.builder()
        .id(faq.getId())
        // .userName(faq.getUser().getName())
        .content(faq.getContent())
        .createdAt(faq.getCreatedAt().format(DATE_FORMATTER))
        .answers(answerResponses)
        .build();
  }

  private static AnswerResponse toAnswerResponse(Answer answer) {
    return AnswerResponse.builder()
        .id(answer.getId())
        // .userName(answer.getFaq().getUser().getName())
        .content(answer.getContent())
        .createdAt(answer.getCreatedAt().format(DATE_FORMATTER))
        .build();
  }
}
