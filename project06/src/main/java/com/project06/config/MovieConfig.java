package com.project06.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MovieConfig {

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
    
    // 필요한 다른 빈 설정들을 여기에 추가할 수 있습니다.
}
