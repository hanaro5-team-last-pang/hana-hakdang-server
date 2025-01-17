package com.hanahakdangserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

  /**
   * Clock을 사용하면 테스트 시에 시간을 주입하여 제어할 수 있다.
   *
   * @return Clock
   */

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone(); // 시스템 기본 시간대 사용
  }
}

