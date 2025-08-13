package com.rookies4.MySpringbootLab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MySpringbootLabApplication {

	public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySpringbootLabApplication.class);
        //Application 타입을 변경하기
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);
    }

    @Bean
    public String hello() {
        return "Hello SpringBoot";
    }
	}

