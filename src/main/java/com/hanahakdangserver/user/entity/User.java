package com.hanahakdangserver.user.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.utils.TimeBaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "uuser")
@AllArgsConstructor
@Builder
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
  private LocalDate birthDate;

  @Column(length = 2048)
  private String profileImageUrl;

  @Column
  @Builder.Default
  private boolean isApproved = false;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isActive = true;
}
