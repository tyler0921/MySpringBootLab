package com.rookies4.MySpringbootLab.config;

import com.rookies4.MySpringbootLab.config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public MyEnvironment myEnvironment() {
        return new MyEnvironment("개발 환경");
    }
}
