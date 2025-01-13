package com.hanahakdangserver.faq.entity;

import jakarta.persistence.*;
import lombok.*;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.utils.TimeBaseEntity;

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
