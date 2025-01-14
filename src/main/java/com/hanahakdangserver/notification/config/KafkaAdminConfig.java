package com.hanahakdangserver.notification.config;

import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaAdminConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServerUrl;

  @Bean
  public AdminClient adminClient() {
    Map<String, Object> configs = Map.of(
        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerUrl
    );
    return AdminClient.create(configs);
  }

}
