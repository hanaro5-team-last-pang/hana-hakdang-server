package com.hanahakdangserver.faq.dto;

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

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "FAQ 응답 DTO")
public class FaqResponse {

  @Schema(description = "FAQ ID", example = "1")
  private Long id;

  @Schema(description = "유저 이름", example = "육지은")
  private String userName;

  @Schema(description = "유저 이미지 URL", example = "http://example.com/profile.jpg")
  private String imageUrl;

  @Schema(description = "문의 내용", example = "이 강의의 수업 자료는 어디서 받을 수 있나요?")
  private String content;

  @Schema(description = "문의 작성 시간", example = "2025-01-20T10:00:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;


  @Schema(description = "답변 리스트")
  private List<AnswerResponse> answers;
}
