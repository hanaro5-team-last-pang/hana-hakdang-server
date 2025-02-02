package com.hanahakdangserver.notification.service;


import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hanahakdangserver.common.SnowFlakeGenerator;
import com.hanahakdangserver.kafka.KafkaNotificationConsumer;
import com.hanahakdangserver.kafka.KafkaProducer;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.notification.dto.NotificationMessage;
import com.hanahakdangserver.notification.entity.Notification;
import com.hanahakdangserver.notification.repository.EmitterRepository;
import com.hanahakdangserver.notification.repository.NotificationRepository;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.lecture.enums.LectureResponseExceptionEnum.LECTURE_NOT_FOUND;
import static com.hanahakdangserver.user.enums.UserResponseExceptionEnum.USER_NOT_FOUND;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

  private final static String KAFKA_TOPIC = "notification"; // topic 이름
  private static final String SSE_NAME = "sse-notification";
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

  private final KafkaProducer<NotificationMessage> notificationProducer;
  private final KafkaNotificationConsumer notificationConsumer;

  private final UserRepository userRepository;
  private final LectureRepository lectureRepository;
  private final NotificationRepository notificationRepository;
  private final EmitterRepository emitterRepository;
  private final SnowFlakeGenerator snowFlakeGenerator;

  public SseEmitter createEmitter(Long userId) {

    String emitterId = String.valueOf(snowFlakeGenerator.nextId());
    SseEmitter emitter = emitterRepository.saveEmitter(userId, emitterId,
        new SseEmitter(DEFAULT_TIMEOUT));

    // SseEmitter가 완료되거나 타임아웃될 때 해당 SseEmitter를 emitterRepository에서 삭제
    emitter.onCompletion(() -> emitterRepository.deleteEmitter(userId, emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteEmitter(userId, emitterId));

    // 첫 접속 시 503 에러를 방지하기 위한 더미 이벤트 전송
    String eventId = "CreatedAt:" + System.currentTimeMillis();
    sendNotification(userId, emitter, eventId, emitterId, "EventStream Created.");

    // TODO : 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방

    return emitter;
  }

  private void createNotification(Long userId, String type, String content) {

    User user = userRepository.findById(userId)
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);
    // DB 저장
    Notification notification = Notification.builder().user(user).type(type).content(content)
        .build();
    saveNotification(notification);

    // Kafka 발행
    NotificationMessage notificationMessage = NotificationMessage.builder()
        .notificationId(notification.getId())
        .receiverId(user.getId())
        .type(type).content(content).isSeen(false).createdAt(notification.getCreatedAt()).build();
    publishNotification(notificationMessage);
  }

  /**
   * 카프카 notification 토픽에 메시지 발행
   *
   * @param notificationMessage 카프카에 발행(publish)될 메시지
   */
  private void publishNotification(NotificationMessage notificationMessage) {
    notificationProducer.publish(KAFKA_TOPIC, notificationMessage);

    log.info("알림 카프카로 전달 - topic: {}, notification id: {}", KAFKA_TOPIC,
        notificationMessage.getNotificationId());
  }

  @Transactional
  protected void saveNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  @KafkaListener(topics = KAFKA_TOPIC, groupId = "notified-group")
  public void listen(ConsumerRecord<Integer, byte[]> record) {
    NotificationMessage message = notificationConsumer.consume(record);

    Map<String, SseEmitter> emitters = emitterRepository.findEmittersByUserId(
        message.getReceiverId());
    emitters.forEach((emitterId, emitter) -> {
      log.info("카프카에서 가져온 알림 전송 - emitter id: {}", emitterId);
      sendNotification(message.getReceiverId(), emitter, message.getNotificationId().toString(),
          emitterId, message);
    });
  }

  /**
   * 클라이언트와 연결된 Emitter 객체에 알림 send
   *
   * @param userId    클라이언트의 user id
   * @param emitter   클라이언트와 연결된 Emitter 객체
   * @param eventId   이벤트(알림) id
   * @param emitterId Emitter 객체 id
   * @param data      알림 DTO or 첫 연결 메시지
   */
  private void sendNotification(Long userId, SseEmitter emitter, String eventId, String emitterId,
      Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(SSE_NAME)
          .data(data)
          .reconnectTime(3000L)  // 클라이언트에서 연결이 끊기고 3초마다 재연결을 시도
      );

      log.info("클라이언트에게 알림 전송 - event id: {}", eventId);

    } catch (IOException exception) {
      emitterRepository.deleteEmitter(userId, emitterId);
    }
  }

  /**
   * 멘토가 강의 시작시 수강신청한 멘티들에게 알림 발송
   *
   * @param receiverId 알림받을 사람의 user id
   * @param lectureId  시작한 강의의 lecture id
   */
  public void noticeLectureStarted(Long receiverId, Long lectureId) {
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(LECTURE_NOT_FOUND::createResponseStatusException);
    String content = "수강신청 하신 <" + lecture.getTitle() + "> 강의가 지금 시작되었습니다!";
    createNotification(receiverId, "LECTURE_STARTED", content);
  }
}
