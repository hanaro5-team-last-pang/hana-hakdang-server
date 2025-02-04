package com.hanahakdangserver.card.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hanahakdangserver.auth.security.CustomUserDetails;
import com.hanahakdangserver.card.dto.ProfileCardRequest;
import com.hanahakdangserver.card.dto.ProfileCardResponse;
import com.hanahakdangserver.card.entity.Card;
import com.hanahakdangserver.card.repository.CardRepository;
import com.hanahakdangserver.lecture.entity.Lecture;
import com.hanahakdangserver.lecture.repository.LectureRepository;
import com.hanahakdangserver.user.entity.CareerInfo;
import com.hanahakdangserver.user.entity.User;
import com.hanahakdangserver.user.enums.Role;
import com.hanahakdangserver.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

  @InjectMocks
  private CardService cardService;

  @Mock
  private CardRepository cardRepository;

  @Mock
  private LectureRepository lectureRepository;

  @Mock
  private UserRepository userRepository;

  private CustomUserDetails customUserDetails;
  private User user;
  private Card card;

  /**
   * 공통 설정 : 시큐리티에 유저를 임의 등록 후 진행
   */
  @BeforeEach
  void setup() {
    customUserDetails = CustomUserDetails.builder()
        .id(1L)
        .email("haneul0509@gmail.com")
        .password("1234")
        .isActive(true)
        .authority(new SimpleGrantedAuthority("ROLE_MENTOR"))
        .build();

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        customUserDetails,
        null,
        customUserDetails.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authRequest);

    CareerInfo careerInfo = CareerInfo.builder()
        .branchName("성수역점")
        .code("12345678")
        .position("SENIOR_MANAGER")
        .startDate(LocalDate.parse("2021-11-25"))
        .build();

    user = User.builder()
        .id(customUserDetails.getId())
        .email(customUserDetails.getUsername())
        .password("1234")
        .isActive(customUserDetails.isEnabled())
        .role(Role.MENTOR)
        .careerInfo(careerInfo)
        .build();

    card = Card.builder()
        .mentor(user)
        .shortIntroduction("안녕하세요! 이하늘 멘토입니다.")
        .build();
  }


  @DisplayName("명함 생성을 시도한다.")
  @Test
  void when_CreateUserCard_expect_success() throws IOException {
    // given
    when(userRepository.findByEmail(customUserDetails.getUsername())).thenReturn(Optional.of(user));

    // when
    cardService.create(customUserDetails.getUsername());

    // then
    verify(cardRepository, times(1)).save(any(Card.class));
  }

  @DisplayName("명함 조회를 시도한다.")
  @Test
  void when_GetUserCard_expect_success() throws IOException {
    // given
    Lecture lecture = Lecture.builder()
        .id(1L)
        .mentor(user)
        .build();
    when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
    when(cardRepository.findByMentorId(lecture.getMentor().getId())).thenReturn(Optional.of(card));

    // when
    ProfileCardResponse profileCardResponse = cardService.get(1L);

    // then
    assertNotNull(profileCardResponse);
    verify(cardRepository, times(1)).findByMentorId(customUserDetails.getId());
  }


  @DisplayName("명함 수정을 시도한다.")
  @Test
  void when_UpdateUserCard_expect_success() throws IOException {
    // given
    when(cardRepository.findByMentorId(1L)).thenReturn(Optional.of(card));

    List<Map<String, String>> simpleInfoList = new ArrayList<>();
    Map<String, String> careerMap = new HashMap<>();
    careerMap.put("key", "경력");
    careerMap.put("value", "20년");
    simpleInfoList.add(careerMap);

    ProfileCardRequest profileCardRequest = ProfileCardRequest.builder()
        .shortIntroduction("안녕하세요! 삼하늘 멘토입니다")
        .simpleInfo(simpleInfoList)
        .build();
    // when
    cardService.update(user.getId(), profileCardRequest);

    // then
    verify(cardRepository, times(1)).save(any(Card.class));
  }

}
