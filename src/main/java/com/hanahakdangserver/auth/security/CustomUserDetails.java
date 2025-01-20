package com.hanahakdangserver.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import com.hanahakdangserver.user.entity.User;

/**
 * Spring Security의 UserDetails 인터페이스를 구현하여 User 엔티티에 맞게 커스터마이징한 클래스
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRole() == null ?
        List.of() :
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
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
    return user.getIsActive();
  }

  public User getUser() {
    return user;
  }
}
