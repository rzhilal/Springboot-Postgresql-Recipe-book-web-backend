package com.arutalalab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.arutalalab", "lib.minio"})
@EnableJpaAuditing
@EnableConfigurationProperties
public class ArutalalabApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArutalalabApplication.class, args);
    }
}
