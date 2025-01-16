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
public class ReviewResponse {

  private Long id;                 // 리뷰 ID
  private Long lectureId;          // 강의 ID
  private Long userId;             // 작성자 ID
  private String userName;         // 작성자 이름
  private String lectureTitle;     // 강의 제목
  private String content;          // 리뷰 내용
  private Double score;           // 평점
  private String createdAt;        // 작성 시간 (yyyy-MM-dd 형식)
  private String updatedAt;        // 수정 시간 (yyyy-MM-dd 형식)

}

