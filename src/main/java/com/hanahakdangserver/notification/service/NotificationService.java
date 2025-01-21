package com.hanahakdangserver.notification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hanahakdangserver.kafka.KafkaProducer;
import com.hanahakdangserver.notification.dto.NotificationMessage;
import com.hanahakdangserver.notification.entity.Notification;
import com.hanahakdangserver.notification.repository.NotificationRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

  @Value("${notification.topic-name}")
  private String notificationTopicName;

  private final KafkaProducer<NotificationMessage> notificationProducer;

  private final NotificationRepository notificationRepository;

  private void publishNotification(NotificationMessage notificationMessage) {
    notificationProducer.publish(notificationTopicName, notificationMessage);
  }

  private void saveNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  //  TODO: 아래와 같이 상황에 맞는 알림 API 추가
  public void noticeLectureStart() {

  }
}
