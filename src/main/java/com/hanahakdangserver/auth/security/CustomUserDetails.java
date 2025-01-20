package com.hanahakdangserver.auth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security의 UserDetails 인터페이스를 구현하여 User 엔티티에 맞게 커스터마이징한 클래스
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  @Getter
  private Long id;
  private String email;
  private String password;
  private Boolean isActive;
  private final GrantedAuthority authority;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(authority); // authority가 하나라고 가정

    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  //회원 이용(탈퇴)상태
  @Override
  public boolean isEnabled() {
    return isActive;
  }

}
