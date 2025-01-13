package com.hanahakdangserver.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanahakdangserver.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  /**
   * 특정 유저의 읽지 않은 알림 조회
   *
   * @param userId 유저 ID
   * @return 읽지 않은 알림 리스트
   */
  @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isSeen = false")
  List<Notification> findByUserIdAndIsSeenFalse(@Param("userId") Long userId);

}
