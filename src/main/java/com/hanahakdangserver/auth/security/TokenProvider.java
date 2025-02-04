package com.hanahakdangserver.auth.security;

import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Value("${spring.jwt.token.expiration_time}")
  private Long TOKEN_EXPIRE_TIME;

  private final CustomUserDetailsService customUserDetailsService;

  // 액세스 토큰 생성
  public String generateAccessToken(Long userId, String userEmail) {

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    SecretKey encryptedKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    return Jwts.builder()
        .claim("id", userId) // 원래는 user id를 직접 넣지는 않으나 웹소켓 서버에서 사용하기 위해 부득이하게 추가
        .claim("email", userEmail)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(encryptedKey, SignatureAlgorithm.HS512)
        .compact();
  }

  public Authentication getUserAuthentication(String jwt) {
    UserDetails userDetails =
        this.customUserDetailsService.loadUserByUsername(this.getUsername(jwt).orElse(null));
    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  public Optional<String> getUsername(String token) {
    try {
      Object email = this.parseClaims(token).get("email");
      return email instanceof String ? Optional.of((String) email) : Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    try {
      Claims claims = this.parseClaims(token);
      boolean isValid = !claims.getExpiration().before(new Date());
      log.info("유효한 토큰 여부: {}", isValid);
      return isValid;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 토큰: {}", token);
      return false;
    } catch (Exception e) {
      log.warn("유효하지 않은 토큰: {}", token);
      return false;
    }
  }

  public Claims parseClaims(String token) {
    SecretKey encryptedKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    try {
      return Jwts.parserBuilder().
          setSigningKey(encryptedKey).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      log.warn("JWT가 만료되었습니다: {}", e.getMessage());
      return e.getClaims();
    } catch (Exception e) {
      log.error("JWT 파싱 중 오류 발생: {}", e.getMessage());
      throw e; // validateToken 호출하는 쪽에서 이 예외를 캐치하도록 함
    }
  }

  // request 헤더에서 token 가져오기
  public String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    } else {
      return null;
    }
  }

  // 토큰의 남은 시간 가져오기
  public Long calculateRemainingTime(Date expiration) {
    Date now = new Date();
    return expiration.getTime() - now.getTime();
  }
}
