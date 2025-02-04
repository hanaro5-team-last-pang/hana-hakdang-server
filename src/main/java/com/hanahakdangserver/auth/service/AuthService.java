package com.hanahakdangserver.auth.service;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanahakdangserver.auth.dto.EmailCheckDTO;
import com.hanahakdangserver.auth.dto.EmailCheckRequest;
import com.hanahakdangserver.auth.dto.LoginRequest;
import com.hanahakdangserver.auth.dto.LoginResponse;
import com.hanahakdangserver.auth.dto.MenteeSignupRequest;
import com.hanahakdangserver.auth.dto.MentorSignupRequest;
import com.hanahakdangserver.auth.mapper.AuthMapper;
import com.hanahakdangserver.auth.security.TokenProvider;
import com.hanahakdangserver.email.service.EmailService;
import com.hanahakdangserver.redis.RedisBoundHash;
import com.hanahakdangserver.user.dto.RandomCareerInfoDTO;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.mapper.CareerInfoMapper;
import com.hanahakdangserver.user.mapper.UserMapper;
import com.hanahakdangserver.user.repository.CareerInfoRepository;
import com.hanahakdangserver.user.repository.UserRepository;
import com.hanahakdangserver.user.utils.CareerInfoGenerator;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.EMAIL_CHECK_EXPIRED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.EMAIL_DUPLICATED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.EMAIL_NOT_CONFIRMED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.EMAIL_NOT_FOUND;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.PASSWORD_NOT_MATCHED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.TOKEN_NOT_MATCHED;
import static com.hanahakdangserver.auth.enums.AuthResponseExceptionEnum.USER_NOT_FOUND;
import static com.hanahakdangserver.auth.utils.AuthUtils.generateToken;

@Log4j2
@Service
@Transactional(readOnly = true)
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final CareerInfoRepository careerInfoRepository;
  private final RedisBoundHash<EmailCheckDTO> redisBoundHash;
  private final EmailService emailService;
  private final TokenProvider tokenProvider;
  @Value("${mail.expire-minute}")
  private Long mailExpireTime;


  public AuthService(UserRepository userRepository, CareerInfoRepository careerInfoRepository,
      RedisTemplate<String, ?> redisTemplate,
      String emailCheckHashKey, EmailService emailService, ObjectMapper objectMapper,
      PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.careerInfoRepository = careerInfoRepository;
    this.redisBoundHash = new RedisBoundHash<>(emailCheckHashKey, redisTemplate, objectMapper);
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.tokenProvider = tokenProvider;
  }

  /**
   * 레디스 해시로부터 {@link com.hanahakdangserver.auth.dto.EmailCheckDTO}를 가져옵니다.
   *
   * @param email 이메일
   * @return {@link com.hanahakdangserver.auth.dto.EmailCheckDTO}
   * @throws ResponseStatusException 레디스 해시로부터 값을 찾을 수 없을 때 발생
   */
  private EmailCheckDTO getEmailCheckDTO(String email) throws ResponseStatusException {
    return redisBoundHash.get(email, EmailCheckDTO.class)
        .orElseThrow(EMAIL_NOT_FOUND::createResponseStatusException);
  }

  private boolean isDuplicatedEmail(String email) {
    return userRepository.existsByEmailAndIsActiveTrue(email);
  }

  private boolean isNotPasswordMatched(String password, String confirmedPassword) {
    return !password.equals(confirmedPassword);
  }

  private void isPasswordMatches(String rawPassword, String encodedPassword)
      throws ResponseStatusException {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw PASSWORD_NOT_MATCHED.createResponseStatusException();
    }
  }

  /**
   * 멘티 회원가입
   *
   * @param menteeSignupRequest 멘티 회원가입 요청
   * @throws ResponseStatusException 회원가입 실패 발생
   */
  @Transactional(readOnly = false)
  public void signupMentee(MenteeSignupRequest menteeSignupRequest)
      throws ResponseStatusException {
    String email = menteeSignupRequest.getEmail();

    EmailCheckDTO emailCheckDTO = getEmailCheckDTO(email);

    if (!emailCheckDTO.isConfirmed()) {
      throw EMAIL_NOT_CONFIRMED.createResponseStatusException();
    }

    if (isDuplicatedEmail(email)) {
      throw EMAIL_DUPLICATED.createResponseStatusException();
    }

    if (isNotPasswordMatched(menteeSignupRequest.getPassword(),
        menteeSignupRequest.getConfirmedPassword())) {
      throw PASSWORD_NOT_MATCHED.createResponseStatusException();
    }

    redisBoundHash.delete(email);
    menteeSignupRequest = AuthMapper.toMenteeSignupRequest(
        menteeSignupRequest,
        passwordEncoder.encode(menteeSignupRequest.getPassword()));
    userRepository.save(UserMapper.toEntity(menteeSignupRequest));
  }

  /**
   * 멘토 회원가입
   *
   * @param mentorSignupRequest 멘토 회원가입 요청
   * @throws ResponseStatusException 회원가입 실패 시 발생
   */
  @Transactional(readOnly = false)
  public void signUpMentor(MentorSignupRequest mentorSignupRequest) throws ResponseStatusException {
    String email = mentorSignupRequest.getEmail();

    EmailCheckDTO emailCheckDTO = getEmailCheckDTO(email);

    if (!emailCheckDTO.isConfirmed()) {
      throw EMAIL_NOT_CONFIRMED.createResponseStatusException();
    }

    if (isDuplicatedEmail(email)) {
      throw EMAIL_DUPLICATED.createResponseStatusException();
    }

    if (isNotPasswordMatched(mentorSignupRequest.getPassword(),
        mentorSignupRequest.getConfirmedPassword())) {
      throw PASSWORD_NOT_MATCHED.createResponseStatusException();
    }

    redisBoundHash.delete(email);
    mentorSignupRequest = AuthMapper.toMentorSignupRequest(
        mentorSignupRequest,
        passwordEncoder.encode(mentorSignupRequest.getPassword()));

    RandomCareerInfoDTO randomCareerInfoDTO = CareerInfoGenerator.getRandomCareerInfo();
    CareerInfo careerInfo = careerInfoRepository.save(CareerInfoMapper.toEntity(randomCareerInfoDTO,
        mentorSignupRequest.getCode()));

    userRepository.save(UserMapper.toEntity(mentorSignupRequest, careerInfo));
  }

  /**
   * 이메일 인증을 위해 이메일을 전송합니다.
   *
   * @param emailCheckRequest 이메일 인증 요청
   */
  public void sendEmail(EmailCheckRequest emailCheckRequest) {
    if (isDuplicatedEmail(emailCheckRequest.getEmail())) {
      throw EMAIL_DUPLICATED.createResponseStatusException();
    }

    String token = generateToken();
    EmailCheckDTO emailCheckDTO = EmailCheckDTO.builder()
        .token(token)
        .expireTime(LocalDateTime.now().plusMinutes(mailExpireTime))
        .build();
    emailService.send(emailCheckRequest.getEmail(), token);
    redisBoundHash.put(emailCheckRequest.getEmail(), emailCheckDTO);
  }

  /**
   * 이메일 인증 확인을 처리합니다. 레디스 해시에서 일치하는 이메일을 가져와 만료시간, 토큰 일치 여부에 따라 인증합니다.
   *
   * @param email
   * @param authToken
   * @throws ResponseStatusException 이메일 인증 실패 시 예외
   */

  public void verifyEmail(String email, String authToken) throws ResponseStatusException {

    EmailCheckDTO emailCheckDTO = getEmailCheckDTO(email);

    LocalDateTime expireTime = emailCheckDTO.getExpireTime();
    if (expireTime.isBefore(LocalDateTime.now())) {
      throw EMAIL_CHECK_EXPIRED.createResponseStatusException();
    }

    String receivedToken = authToken;
    String savedToken = emailCheckDTO.getToken();
    if (!receivedToken.equals(savedToken)) {
      throw TOKEN_NOT_MATCHED.createResponseStatusException();
    }

    EmailCheckDTO newEmailCheckDTO = EmailCheckDTO.builder()
        .token(receivedToken)
        .confirmed(true)
        .build();

    redisBoundHash.put(email, newEmailCheckDTO);
  }

  @Transactional
  public LoginResponse login(LoginRequest loginRequest) throws ResponseStatusException {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(USER_NOT_FOUND::createResponseStatusException);
    isPasswordMatches(loginRequest.getPassword(), user.getPassword()); // 비밀번호 일치하는지 체크

    String accessToken = tokenProvider.generateAccessToken(
        user.getId(), loginRequest.getEmail()); // access token 생성

    return LoginResponse.builder().accessToken(accessToken).build();
  }
}
