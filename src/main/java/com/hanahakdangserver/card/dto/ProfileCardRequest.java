package com.hanahakdangserver.card.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "계정 정보 수정 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ProfileCardRequest {

  @Schema(description = "프로필 사진 상단에 있는 짧은 자기소개", example = "안녕하세요 중씨입니다.")
  private String shortIntroduction;

  @Schema(description = "짧은 소개", example = "{\"경력\": \"20년\", \"스킬\":\"SPRING, REACT\"}")
  private Map<String, String> simpleInfo;

  @Schema(description = "강의 카테고리 ENUM", example = "FINANCIAL_PRODUCTS")
  private Map<String, String> detailInfo;

}
