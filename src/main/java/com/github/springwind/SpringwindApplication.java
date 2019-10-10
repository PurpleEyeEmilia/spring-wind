package com.github.springwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 15:38
 * @Desc
 */
@SpringBootApplication
@EnableConfigurationProperties
public class SpringwindApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringwindApplication.class, args);
    }

}
