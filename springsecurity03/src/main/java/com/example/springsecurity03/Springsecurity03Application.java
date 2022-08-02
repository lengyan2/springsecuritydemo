package com.example.springsecurity03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Springsecurity03Application {

    public static void main(String[] args) {
        SpringApplication.run(Springsecurity03Application.class, args);
    }

}
