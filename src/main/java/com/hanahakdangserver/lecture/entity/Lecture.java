package com.hanahakdangserver.lecture.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.lecture.utils.IntegerListConverter;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.mixin.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Lecture extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //  TODO : UserDetails 엔티티 구현되면 적용할 예정
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "mentor_id", nullable = false)
//  private User mentor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classroom_id", nullable = false)
  private Classroom classroom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private Integer duration;

  @Column(nullable = false)
  private Integer maxParticipants;

  @Column(columnDefinition = "text", nullable = true)
  private String description;

  @Convert(converter = IntegerListConverter.class)
  @Column(name = "tag_list", nullable = false)
  private List<Integer> tagList;

  @Column(nullable = true)
  private String thumbnailUrl;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isFull = false;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isCanceled = false;

  public void updateIsFull(boolean isFull) {
    this.isFull = isFull;
  }
}
