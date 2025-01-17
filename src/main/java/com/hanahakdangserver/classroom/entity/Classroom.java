package com.hanahakdangserver.classroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Classroom {

  @Id
  private Long id;

  //  TODO : UserDetails 엔티티 구현되면 적용할 예정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mentor_id", nullable = false)
  private User mentor;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isUsed = true;

  public void updateIsUsed(Boolean isUsed) {
    this.isUsed = isUsed;
  }

}
