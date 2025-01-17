package com.hanahakdangserver.lecture.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "강의 상세 정보 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class LectureDetailDTO {

  @Schema(description = "강의 Id", example = "1")
  private Long lecture_id;

  @Schema(description = "멘토 이름", example = "정중일")
  private String mentor_name;

  @Schema(description = "강의 카테고리", example = "디지털 교육")
  private String category;

  @Schema(description = "강의 명칭", example = "정중일과 함께 하는 하나원큐앱 정복하기")
  private String title;

  @Schema(description = "강의 소개", example = "안녕하세요! 여러분의 멘토, 정중일입니다.")
  private String description;

  @Schema(description = "강의 시작시간", example = "2025-01-19 11:30:00")
  private LocalDateTime start_date;

  @Schema(description = "강의 진행 시간", example = "2")
  private Integer duration;

  @Schema(description = "현재 수강신청한 인원", example = "4")
  private Integer curr_participants;

  @Schema(description = "수강신청 가능한 인원", example = "6")
  private Integer max_participants;

  @Schema(description = "수강신청 마감 여부", example = "false")
  private Boolean is_full;

  @Schema(description = "강의 썸네일 url", example = "www.hanaro-hanahakdang.com")
  private String thumbnail_img_url;
}
