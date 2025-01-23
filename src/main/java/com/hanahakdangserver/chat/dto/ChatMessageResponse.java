package com.hanahakdangserver.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "채팅 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ChatMessageResponse {

  @Schema(description = "강의ID", example = "123")
  private Long lectureId;

  @Schema(description = "유저ID", example = "1")
  private Long userId;

  @Schema(description = "유저 이름", example = "김동연")
  private String username;

  @Schema(description = "채팅 내용", example = "라스트팡 팀 화이팅!!")
  private String body;

  @Schema(description = "채팅 시간", example = "11:40")
  private LocalDateTime timestamp;

}


