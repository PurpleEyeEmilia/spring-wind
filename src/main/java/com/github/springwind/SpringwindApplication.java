package com.github.springwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringwindApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringwindApplication.class, args);
    }

}
