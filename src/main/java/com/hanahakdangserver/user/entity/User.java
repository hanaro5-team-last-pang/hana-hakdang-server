package com.hanahakdangserver.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hanahakdangserver.mixin.TimeBaseEntity;
import com.hanahakdangserver.user.enums.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "uuser")
@AllArgsConstructor
@Builder
public class User extends TimeBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private CareerInfo careerInfo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private LocalDate birthDate;

  @Column(length = 2048)
  private String profileImageUrl;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isActive = true;


  public User update(String profileImageUrl, String newPassword) {
    return User.builder()
        .id(this.id)
        .careerInfo(this.careerInfo)
        .role(this.role)
        .name(this.name)
        .email(this.email)
        .password(newPassword != null ? newPassword : this.password)
        .birthDate(this.birthDate)
        .profileImageUrl(profileImageUrl != null ? profileImageUrl : this.profileImageUrl)
        .isActive(this.isActive)
        .build();
  }
}
