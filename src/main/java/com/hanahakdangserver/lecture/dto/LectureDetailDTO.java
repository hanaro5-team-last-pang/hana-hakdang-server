package com.hanahakdangserver.lecture.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(description = "강의 상세 정보 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class LectureDetailDTO {

  @Schema(description = "강의 Id", example = "1")
  private Long lectureId;

  @Schema(description = "멘토 이름", example = "정중일")
  private String mentorName;

  @Schema(description = "강의 카테고리", example = "디지털 교육")
  private String category;

  @Schema(description = "강의 태그 목록", example = "[\"정기예금\", \"적금\"]")
  private List<String> tags;

  @Schema(description = "강의 명칭", example = "정중일과 함께 하는 하나원큐앱 정복하기")
  private String title;

  @Schema(description = "강의 소개", example = "안녕하세요! 여러분의 멘토, 정중일입니다.")
  private String description;

  @Schema(description = "강의 시작시간", example = "2025-01-19 11:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime startTime;

  @Schema(description = "강의 예상 종료시간", example = "2025-01-19 12:30:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime endTime;

  @Schema(description = "강의 예상 진행 시간", example = "1")
  private Integer duration;

  @Schema(description = "현재 수강신청한 인원", example = "4")
  private Integer currParticipants;

  @Schema(description = "수강신청 가능한 인원", example = "6")
  private Integer maxParticipants;

  @Schema(description = "수강신청 마감 여부", example = "false")
  private Boolean isFull;

  @Schema(description = "강의 썸네일 url", example = "www.hanaro-hanahakdang.com")
  private String thumbnailImgUrl;

  @Schema(description = "수강신청 버튼 활성화 여부; 비로그인 유저&수강신청 안 한 멘티면 NOT_ENROLLED, 멘토는 MENTOR, 수강신청 한 멘티면 ENROLLED", example = "ENROLLED")
  private String enrollStatus;
}
