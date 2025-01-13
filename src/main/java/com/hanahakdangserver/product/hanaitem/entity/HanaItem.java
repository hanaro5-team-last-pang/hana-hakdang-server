package com.hanahakdangserver.product.hanaitem.entity;

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

import com.hanahakdangserver.product.tag.entity.Tag;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class HanaItem extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Tag tag;

  @Column(nullable = false, length = 255)
  private String itemTitle;

  @Column(columnDefinition = "TEXT")
  private String itemContent;

  @Column(length = 2048)
  private String hanaUrl;
}
