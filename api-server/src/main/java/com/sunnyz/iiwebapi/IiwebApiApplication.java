package com.sunnyz.iiwebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class IiwebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IiwebApiApplication.class, args);
    }

}
