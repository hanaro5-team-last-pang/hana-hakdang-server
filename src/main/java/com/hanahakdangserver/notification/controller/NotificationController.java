package com.hanahakdangserver.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.notification.service.NotificationService;

@Tag(name = "알림", description = "알림 API 목록")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(summary = "SSE 구독", description = "실시간 알림을 받기 위해 최초로 SSE 구독을 시도한다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "SSE 객체가 반환됩니다.")
  })
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> subscribeSSE(
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    SseEmitter sseEmitter = notificationService.createEmitter(userDetails.getId());
    return ResponseEntity.ok(sseEmitter);
  }
}
