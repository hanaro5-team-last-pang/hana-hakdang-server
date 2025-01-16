package com.hanahakdangserver.review.dto;


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
public class ReviewRequest {

  private Long userId;    // 리뷰 작성자 ID
  private String content; // 리뷰 내용
  private Integer score;  // 평점
}

