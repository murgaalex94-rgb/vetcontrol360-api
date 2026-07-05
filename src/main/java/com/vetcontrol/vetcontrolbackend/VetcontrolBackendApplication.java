package com.vetcontrol.vetcontrolbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VetcontrolBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(VetcontrolBackendApplication.class, args);
    }
}
