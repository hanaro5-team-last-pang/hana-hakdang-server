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

@Schema(description = "명함 수정 요청")
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

  @Schema(description = "구체적인 소개", example = "{\"구체적인 경험\": \"하나은행에서 백엔드 개발자를 20년 동안 하다가 한국은행에서 10년동안 인프라를 구축했습니다.\", \"추가사항\":\"강사 경력을 통해 방대한 지식을 알기 쉽게 설명해드리겠습니다.\"}")
  private Map<String, String> detailInfo;

}
