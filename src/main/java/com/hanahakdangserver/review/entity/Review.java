package com.hanahakdangserver.review.entity;

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
public class Review extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Integer score;


}
