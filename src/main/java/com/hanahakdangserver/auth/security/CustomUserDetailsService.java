package com.hanahakdangserver.auth.security;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.repository.UserRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    log.debug("로그인 시 입력받은 이메일 : {}", email);
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
    } else {
      return CustomUserDetails.builder()
          .id(user.get().getId())
          .email(user.get().getEmail())
          .password(user.get().getPassword())
          .isActive(user.get().getIsActive())
          .authority(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().name()))
          .build();
    }
  }
}
