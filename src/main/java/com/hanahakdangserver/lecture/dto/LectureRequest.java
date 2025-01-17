package com.hanahakdangserver.lecture.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hanahakdangserver.lecture.enums.LectureCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "강의 등록 요청")
public class LectureRequest {

  @Schema(description = "강의 카테고리 ENUM", example = "FINANCIAL_PRODUCTS")
  @NotNull(message = "유효하지 않은 카테고리가 입력되었습니다.")
  private LectureCategory category;

  @Schema(description = "강의 제목", example = "중일이와 함께하는 하나원큐앱 부시기")
  @NotBlank(message = "제목은 반드시 입력해야 합니다.")
  private String title;

  @Schema(description = "강의 시작 시간", example = "2025-01-23 10:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime start_time;

  @Schema(description = "강의 진행 시간", example = "2")
  private Integer duration;

  @Schema(description = "최대 수강 가능 인원", example = "4")
  @NotNull(message = "최대 수강 가능 인원 수를 입력해주세요.")
  private Integer max_participants;

  @Schema(description = "강의 설명", example = "안녕하세요~ 여러분의 멘토 정중일입니다.")
  private String description;

  @Schema(description = "강의와 연관된 금융 상품 태그 ID 리스트", example = "[1, 2]")
  private List<Integer> tags;
}
