package com.hanahakdangserver.lecture.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "카테고리 목록 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class LectureCategoriesResponse {

  @Schema(description = "강의 카테고리 목록", example = "[{\"tag\": \"GLOBAL\", \"name\": \"글로벌\"}, {\"tag\": \"FINANCIAL_PRODUCTS\", \"name\": \"금융 상품\"}]")
  private List<LectureCategoriesDetailDTO> categories;

}
