package com.hanahakdangserver.notification.dto;


import java.time.LocalDateTime;

/**
 * 카프카에 발행(publish)될 메시지 타입을 정의
 */
public class NotificationMessage {

  private Long userId;

  private String type;

  private String content;

  private LocalDateTime createdAt;

}
