package com.hanahakdangserver.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.notification.service.NotificationService;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/notificatioins")
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping("/send-notification")
  public ResponseEntity<String> sendNotification(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    notificationService.noticeTestData(userDetails.getId());
    return ResponseEntity.ok("Notification sent successfully");
  }
}
