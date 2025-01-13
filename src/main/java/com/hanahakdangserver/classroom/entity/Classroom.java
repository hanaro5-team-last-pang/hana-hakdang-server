package com.hanahakdangserver.classroom.entity;

import jakarta.persistence.*;
import lombok.*;

import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Classroom extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mentor_id", nullable = false)
  private User mentor;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isUsed = true;


}
