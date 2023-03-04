package com.ok.okbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OkbotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OkbotApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }
}
