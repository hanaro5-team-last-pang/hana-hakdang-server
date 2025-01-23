package com.hanahakdangserver.chat.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.chat.dto.ChatMessageResponse;
import com.hanahakdangserver.chat.service.ChatService;

@Tag(name = "채팅", description = "채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @Operation(summary = "채팅 내역", description = "Redis에 저장된 채팅 내역 반환")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채팅 내역 호출 성공했습니다."),
      @ApiResponse(responseCode = "404", description = "해당 강의실에 채팅 내역이 없습니다.")
  })

  @GetMapping("/{classroomId}/messages")
  public ResponseEntity<?> getChatMessages(@PathVariable Long classroomId) {
    List<ChatMessageResponse> messages = chatService.getChatMessage(classroomId);
    return ResponseEntity.ok(messages);
  }
}
