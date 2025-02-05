package com.hanahakdangserver.notification.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.kafka.KafkaProducer;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.notification.dto.NotificationMessage;
import com.hanahakdangserver.notification.entity.Notification;
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

  private final KafkaProducer<NotificationMessage> notificationProducer;

  private final UserRepository userRepository;
  private final LectureRepository lectureRepository;
  private final NotificationRepository notificationRepository;

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

  public void noticeTestData(Long receiverId) {
    createNotification(receiverId, "TEST", "서버 테스트 알림입니다.");
  }
}
