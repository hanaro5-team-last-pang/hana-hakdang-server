package com.hanahakdangserver.lecture.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

import com.hanahakdangserver.lecture.utils.IntegerListConverter;
import com.hanahakdangserver.lecture.category.entity.Category;
import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Lecture extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mentor_id", nullable = false)
  private User mentor;

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
  private Integer price;

  @Column(nullable = false)
  private Integer maxParticipants;

  @Column(nullable = true)
  private String description;

  @Convert(converter = IntegerListConverter.class)
  @Column(name = "tag_list", nullable = false)
  private List<Integer> tagList;

  @Column(nullable = true)
  private String thumbnailUrl;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isCanceled = false;


}
