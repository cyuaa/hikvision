package com.ulearning.ulms.hk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HkApplication.class, args);
    }

}
