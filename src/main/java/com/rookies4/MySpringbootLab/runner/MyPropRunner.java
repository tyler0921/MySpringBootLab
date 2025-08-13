package com.rookies4.MySpringbootLab.runner;

import com.rookies4.MySpringbootLab.config.vo.MyEnvironment;
import com.rookies4.MySpringbootLab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    private final MyPropProperties myProps;
    private final MyEnvironment myEnvironment;

    @Autowired
    public MyPropRunner(MyPropProperties myProps, MyEnvironment myEnvironment) {
        this.myProps = myProps;
        this.myEnvironment = myEnvironment;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info(myEnvironment.toString()); // 현재 환경 모드 출력
        logger.info("Username: {}", myProps.getUsername());
        logger.info("Port: {}", myProps.getPort());
        logger.debug("이것은 디버그 레벨 로그입니다. 'test' 프로파일에서만 보입니다.");
        logger.info("========================================");
    }
}