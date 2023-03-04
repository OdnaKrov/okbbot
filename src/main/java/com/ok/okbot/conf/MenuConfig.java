package com.ok.okbot.conf;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MenuConfig {
    private final ResourcePatternResolver resourcePatternResolver;
    private final Map<String, String> textToSend = new HashMap<>();

    public MenuConfig(ResourcePatternResolver resourcePatternResolver) throws IOException {
        this.resourcePatternResolver = resourcePatternResolver;
        configureMenuText();
    }

    private void configureMenuText() throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath*:menu_text/*.txt");
        for (Resource resource : resources) {
            log.info("Process file: {}", resource.getFilename());
            String stateLabel = resource.getFilename().substring(0, resource.getFilename().length() - 4);

            try (InputStream inputStream = resource.getInputStream()) {
                textToSend.put(stateLabel, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    public String textToSend(Placeholder placeholder) {
        return textToSend.get(placeholder.getValue());
    }

    private ReplyKeyboardMarkup getMainMenuButtons() {
        KeyboardButton button1 = new KeyboardButton("1");
        KeyboardButton button2 = new KeyboardButton("2");
        KeyboardButton button3 = new KeyboardButton("3");
        return new ReplyKeyboardMarkup(List.of(button1, button2, button3).toArray(KeyboardButton[]::new))
                .resizeKeyboard(true);
    }

    private ReplyKeyboardMarkup getWhatCanWeDoMenuButtons() {
        KeyboardButton button1 = new KeyboardButton("1");
        KeyboardButton button2 = new KeyboardButton("2");
        return new ReplyKeyboardMarkup(List.of(button1, button2).toArray(KeyboardButton[]::new))
                .resizeKeyboard(true);
    }
}
