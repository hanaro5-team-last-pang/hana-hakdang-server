package com.hanahakdangserver.user;

import java.time.LocalDate;

import com.hanahakdangserver.utils.TimeBaseEntity;
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

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
  private LocalDate birthDate;

  @Column(length = 2048)
  private String profileImageUrl;

  @Column
  private boolean isApproved;

  @Column(nullable = false)
  private Boolean isActive;

    public static class UserBuilder {
      public User build() {
        this.isApproved = (this.role == Role.MENTOR);
        this.isActive = true;
        return new User(id, role, name, email, password, birthDate, profileImageUrl, isApproved, isActive);
      }
    }
}
