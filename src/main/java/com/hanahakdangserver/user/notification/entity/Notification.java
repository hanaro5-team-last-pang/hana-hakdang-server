package com.hanahakdangserver.user.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import com.hanahakdangserver.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(nullable = false)
  private User user;

  @Column
  @Builder.Default
  private boolean isSeen = false;

  @Column(nullable = false, length = 255)
  private String type;

  @Column(nullable = false, length = 255)
  private String content;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;
}
