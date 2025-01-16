package com.hanahakdangserver.user.utils;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hanahakdangserver.user.dto.RandomCareerInfoDTO;


public class CareerInfoGeneratorTest {

  @RepeatedTest(10)
  @DisplayName("랜덤 행원 정보 생성 테스트")
  void testGetRandomCareerInfo() {
    RandomCareerInfoDTO randomCareerInfoDTO = CareerInfoGenerator.getRandomCareerInfo();
    assertThat(randomCareerInfoDTO.getStartDate().isBefore(LocalDate.now())).isTrue();
  }
}
