package com.hanahakdangserver.user.card;

import com.hanahakdangserver.user.User;
import com.hanahakdangserver.utils.TimeBaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

public class Card extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(nullable = false)
  private User mentorId;

  @Column(nullable = false, length = 100)
  private String shortIntroduction;

  @Column(columnDefinition = "TEXT")
  private String simpleInfo;

  @Column(columnDefinition = "TEXT")
  private String detailInfo;

  @PrePersist
  public void setDefaultShortIntroduction() {
    if (this.shortIntroduction == null || this.shortIntroduction.isEmpty()) {
      this.shortIntroduction = "안녕하세요! " + mentorId.getName() + " 멘토입니다.";
    }
  }
}
