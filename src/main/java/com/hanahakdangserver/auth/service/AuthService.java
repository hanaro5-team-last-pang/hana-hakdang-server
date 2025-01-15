package com.hanahakdangserver.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanahakdangserver.auth.dto.EmailDTO;
import com.hanahakdangserver.auth.dto.isSendEmailResponse;
import com.hanahakdangserver.auth.mapper.AuthMapper;
import com.hanahakdangserver.email.service.EmailService;
import com.hanahakdangserver.user.dto.MentiSignupRequest;
import com.hanahakdangserver.user.mapper.UserMapper;
import com.hanahakdangserver.user.repository.UserRepository;
import static com.hanahakdangserver.auth.utils.AuthUtils.generateToken;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final RedisService redisService;
  private final EmailService emailService;

  //TODO : 멘티 회원가입. 후에 멘토 회원가입과 통합할 수 있는지 리팩터링 생각해보기
  @Transactional(readOnly = false)
  public ResponseEntity<String> signupMenti(MentiSignupRequest mentiSignupRe) {
    EmailDTO emailDTO = AuthMapper.toDTO(mentiSignupRe.getEmail());

    if (checkDuplicateEmail(emailDTO)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 이메일은 존재합니다.");
    }

    if (!checkEqualPassword(mentiSignupRe.getPassword(),
        mentiSignupRe.getConfirmedPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
    }

    String check = redisService.getCheckByEmail(emailDTO.getEmail());
    if (check == null || check.equals("false")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증이 완료되지 않았습니다.");
    }

    userRepository.save(UserMapper.toMentiEntity(mentiSignupRe));
    return ResponseEntity.status(HttpStatus.CREATED).body("멘티 회원가입 성공");
  }

  public isSendEmailResponse sendEmail(EmailDTO emailDTO) {
    String token = generateToken();
    redisService.saveEmailAndValues(emailDTO.getEmail(), token);
    emailService.send(emailDTO.getEmail(), token);

    return new isSendEmailResponse(true);
  }

  public ResponseEntity<String> vertifyEmail(String email, String authToken) {
    String savedToken = redisService.getTokenByEmail(email);
    if (authToken.equals(savedToken)) {
      redisService.updateCheckToTrue(email);
      return ResponseEntity.status(HttpStatus.OK).body("토큰 인증 성공");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰 인증 실패");
  }

  //이메일 중복 체크
  public boolean checkDuplicateEmail(EmailDTO emailDTO) {
    return !userRepository.findByEmail(emailDTO.getEmail()).isEmpty();
  }

  //비밀번호 일치 체크
  public boolean checkEqualPassword(String password, String comfirmedPassword) {
    return password.equals(comfirmedPassword);
  }
}
