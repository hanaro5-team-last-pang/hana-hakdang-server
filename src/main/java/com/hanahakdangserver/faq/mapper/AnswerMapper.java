package com.hanahakdangserver.faq.mapper;

import com.hanahakdangserver.faq.dto.AnswerResponse;
import com.hanahakdangserver.faq.entity.Answer;

import java.time.format.DateTimeFormatter;

public class AnswerMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss");

  /**
   * Answer 엔티티를 AnswerResponse DTO로 변환
   */
  public static AnswerResponse toDto(Answer answer) {
    return AnswerResponse.builder()
        .id(answer.getId())
        // .userName(answer.getFaq().getUser().getName()) // 답변 작성자 이름
        .content(answer.getContent())
        .createdAt(answer.getCreatedAt().format(DATE_FORMATTER))
        .build();
  }
}
