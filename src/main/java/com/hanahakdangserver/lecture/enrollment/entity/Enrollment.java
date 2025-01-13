package com.hanahakdangserver.lecture.enrollment.entity;

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
public class Enrollment extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private Integer amount;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isCanceled = false;

}
