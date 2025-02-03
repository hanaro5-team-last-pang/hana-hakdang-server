package com.hanahakdangserver.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class NewsConfig {

  @Value("${crawling.news.url}")
  private String crawlingUrl;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(10 * 1024 * 1024)) // 10MB로 설정
        .baseUrl(crawlingUrl)
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
            .responseTimeout(Duration.ofSeconds(20))))
        .build();
  }
}

