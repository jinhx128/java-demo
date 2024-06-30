package com.jinhx.java.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringInit implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("spring init success");
    }

}