package com.ok.okbot.conf;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot("6075740235:AAEhvNgzKB9Cu6yTLqNvUHjuywHgipMnRE4");
    }
}
