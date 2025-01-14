package com.hanahakdangserver.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class HanaItemResponse {

  private Long id;
  private String itemTitle;
  private String itemContent;
  private String hanaUrl;
  private Long lectureId; // 강의 ID
  
}
