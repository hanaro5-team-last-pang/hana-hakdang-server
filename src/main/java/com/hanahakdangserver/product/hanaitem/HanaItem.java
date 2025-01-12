package com.hanahakdangserver.product.hanaitem;

import com.hanahakdangserver.product.tag.Tag;
import com.hanahakdangserver.utils.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HanaItem extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Tag tagId;

  @Column(nullable = false, length = 255)
  private String itemTitle;

  @Column(columnDefinition = "TEXT")
  private String itemContent;

  @Column(length = 2048)
  private String hanaUrl;
}
