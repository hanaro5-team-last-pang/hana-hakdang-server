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
import static com.hanahakdangserver.chat.enums.ChatResponseSuccessEnum.GET_CHAT_MESSAGES_SUCCESS;

@Tag(name = "채팅", description = "채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @Operation(summary = "채팅 내역", description = "Redis에 저장된 채팅 내역 반환")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채팅 내역 호출 성공했습니다."),
  })
  @GetMapping("/{classroomId}/messages")
  public ResponseEntity<?> getChatMessages(@PathVariable Long classroomId) {
    List<ChatMessageResponse> messages = chatService.getChatMessage(classroomId);
    return GET_CHAT_MESSAGES_SUCCESS.createResponseEntity(messages);
  }
}
