package com.hanahakdangserver.card.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "명함 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ProfileCardResponse {

  @Schema(description = "멘토 이름", example = "미스터 중")
  private String mentor_name;

  @Schema(description = "멘토 프로필 사진", example = "https://lastpang-file-bucket.s3.ap-northeast-2.amazonaws.com/55de2de7-58e1-4a1d-a682-d4572b6da31d포차코.png")
  private String profileImageUrl;

  @Schema(description = "프로필 사진 상단에 있는 짧은 자기소개", example = "안녕하세요 정중일 멘토입니다.")
  private String short_introduction;

  @Schema(description = "짧은 소개", example = "[{\"key\": \"경력\", \"value\": \"20년\"}, {\"key\": \"스킬\", \"value\": \"SPRING, REACT\"}]")
  private List<Map<String, String>> simple_info;

  @Schema(description = "구체적인 소개", example = "[{\"key\": \"구체적인 경험\", \"value\": \"하나은행에서 백엔드 개발자를 20년 동안 하다가 한국은행에서 10년동안 인프라를 구축했습니다.\"}, {\"key\": \"추가사항\", \"value\": \"강사 경력을 통해 방대한 지식을 알기 쉽게 설명해드리겠습니다.\"}]")
  private List<Map<String, String>> detail_info;

}
