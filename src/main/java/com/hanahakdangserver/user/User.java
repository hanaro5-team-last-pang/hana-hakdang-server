package com.hanahakdangserver.user;

import java.time.LocalDate;

import com.hanahakdangserver.utils.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "uuser")

public class User extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
  private LocalDate birthDt;

  @Column(length = 2048)
  private String profileImageUrl;

  @Column
  private boolean isApproved;

  @Column(nullable = false)
  private Boolean isActive = true;

  @Builder
  public User(Role role, String name, String email, String password, LocalDate birthDt,
       String profileImageUrl) {
    this.role = role;
    this.email = email;
    this.password = password;
    this.name = name;
    this.birthDt = birthDt;
    this.profileImageUrl = profileImageUrl;
    this.isApproved = (role == Role.MENTOR);
  }
}
