package com.hanahakdangserver.lecture.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.hanahakdangserver.classroom.entity.Classroom;
import com.hanahakdangserver.lecture.enrollment.entity.Enrollment;
import com.hanahakdangserver.mixin.TimeBaseEntity;
import com.hanahakdangserver.user.entity.User;

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

  @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Enrollment> enrollments;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @Column(nullable = false)
  private Integer maxParticipants;

  @Column(columnDefinition = "text", nullable = true)
  private String description;

  @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LectureTag> tagList;

  @Column(nullable = true)
  private String thumbnailUrl;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isFull = false;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isCanceled = false;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isDone = false;

  public void updateStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void updateEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void updateIsFull(boolean isFull) {
    this.isFull = isFull;
  }

  public void updateIsCanceled(boolean isCanceled) {
    this.isCanceled = isCanceled;
  }

  public void updateIsDone(boolean isDone) {
    this.isDone = isDone;
  }

}
