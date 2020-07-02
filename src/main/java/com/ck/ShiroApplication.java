package com.ck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class ShiroApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ShiroApplication.class);
        springApplication.run(args);
    }
}
