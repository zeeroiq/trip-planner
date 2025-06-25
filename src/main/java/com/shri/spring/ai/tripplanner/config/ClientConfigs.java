package com.shri.spring.ai.tripplanner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ClientConfigs {

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .build();
    }
}
