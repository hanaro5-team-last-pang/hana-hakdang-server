package com.hanahakdangserver.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "상품ID", example = "1")
  private Long id;

  @Schema(description = "상품명", example = "3·6·9 정기예금")
  private String itemTitle;

  @Schema(description = "상품 설명", example = "3개월마다 중도해지 혜택 제공")
  private String itemContent;

  @Schema(description = "상품 URL", example = "https://www.kebhana.com/cont/mall")
  private String hanaUrl;

//  필요 없을 거 같아 주석
//  @Schema(description = "강의 ID", example = "1")
//  private Long lectureId;

}