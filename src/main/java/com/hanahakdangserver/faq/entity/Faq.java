package com.hanahakdangserver.faq.entity;

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

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.mixin.TimeBaseEntity;
import com.hanahakdangserver.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Faq extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id")
  private Lecture lecture;

  @Column(nullable = false)
  private String content;


}
