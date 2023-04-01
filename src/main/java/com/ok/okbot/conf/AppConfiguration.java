package com.ok.okbot.conf;

import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfiguration {

    @Value("${okbot.telegramToken}")
    private String botToken;

    @Bean
    public TelegramBot telegramBot() {
        log.info("Bot Token: {}", botToken);
        return new TelegramBot(botToken);
    }
}
