package com.hanahakdangserver.faq.answer.entity;

import jakarta.persistence.*;
import lombok.*;

import com.hanahakdangserver.faq.entity.Faq;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Answer extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "faq_id", nullable = false)
  @NonNull
  private Faq faq;

  @Column(nullable = false)
  private String content;

}
