package com.hanahakdangserver.card.entity;

import java.util.Map;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.hanahakdangserver.mixin.TimeBaseEntity;
import com.hanahakdangserver.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Card extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(nullable = false)
  private User mentor;

  @Column(nullable = false, length = 100)
  private String shortIntroduction;

  @Type(JsonType.class)
  @Column(columnDefinition = "TEXT")
  private Map<String, String> simpleInfo;

  @Type(JsonType.class)
  @Column(columnDefinition = "TEXT")
  private Map<String, String> detailInfo;
}
