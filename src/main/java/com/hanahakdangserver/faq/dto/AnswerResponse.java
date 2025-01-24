package com.hanahakdangserver.faq.dto;

import java.time.LocalDateTime;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "답변 응답 DTO")
public class AnswerResponse {

  @Schema(description = "답변 ID", example = "1")
  private Long id;

  @Schema(description = "유저 이름", example = "한성민")
  private String userName;

  @Schema(description = "작성자 프로필 이미지 URL", example = "https://example.com/user-image.jpg")
  private String imageUrl;

  @Schema(description = "답변 내용", example = "답변 내용입니다")
  private String content;

  @Schema(description = "답변 작성 시간", example = "2025-01-20T10:00:00")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

}
