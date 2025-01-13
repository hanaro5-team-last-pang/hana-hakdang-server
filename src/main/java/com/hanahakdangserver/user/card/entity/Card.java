package com.hanahakdangserver.user.card.entity;

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
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Card extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
