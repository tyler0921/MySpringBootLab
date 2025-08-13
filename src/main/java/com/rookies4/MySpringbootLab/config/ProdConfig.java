package com.rookies4.MySpringbootLab.config;

import com.rookies4.MySpringbootLab.config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod") // "prod" 프로파일이 활성화될 때만 이 설정이 적용됨
@Configuration
public class ProdConfig {

    @Bean
    public MyEnvironment myEnvironment() {
        return new MyEnvironment("운영 환경");
    }
}