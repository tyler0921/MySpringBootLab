package com.rookies4.MySpringbootLab.config.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MyEnvironment {
    private String mode;

    public MyEnvironment(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "현재 모드: " + mode;
    }
}