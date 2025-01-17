package com.hanahakdangserver.enrollment.entity;

import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.mixin.TimeBaseEntity;
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

  //  TODO : UserDetails 엔티티 구현되면 적용할 예정
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id", nullable = false)
//  private User user;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isCanceled = false;

}
