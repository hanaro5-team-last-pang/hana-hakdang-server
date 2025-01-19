package com.hanahakdangserver.faq.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class FaqResponse {

  private Long id;
  private String userName;
  //  private Long lectureId;
  private String content;
  //  private LocalDateTime createdAt;
  private String createdAt;
  private List<AnswerResponse> answers;

}
