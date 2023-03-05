package com.ok.okbot.conf;

import com.google.gson.Gson;
import com.ok.okbot.dto.Partner;
import com.ok.okbot.dto.PartnerConfig;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MenuConfig {
    private final ResourcePatternResolver resourcePatternResolver;
    private final Map<String, String> textToSend = new HashMap<>();
    private final List<Partner> partners = new ArrayList<>();

    public MenuConfig(ResourcePatternResolver resourcePatternResolver) throws IOException {
        this.resourcePatternResolver = resourcePatternResolver;
        configureMenuText();
        loadPartners();
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

    private void loadPartners() throws IOException {
        log.info("Load partners");
        Resource[] resources = resourcePatternResolver.getResources("classpath*:partners/*.json");
        Gson gson = new Gson();
        for (Resource resource : resources) {
            log.info("Process partner file: {}", resource.getFilename());
            try (InputStream inputStream = resource.getInputStream()) {
                PartnerConfig partnerConfig = gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), PartnerConfig.class);
                processPartnerConfig(partnerConfig);
            }
        }
    }

    private void processPartnerConfig(PartnerConfig partnerConfig) throws IOException {
        Resource descriptionResource = resourcePatternResolver
                .getResource("classpath:partners/description/" + partnerConfig.getDescriptionSource());

        String description;
        try (InputStream inputStream = descriptionResource.getInputStream()) {
            description = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        byte[] image = null;
        if (partnerConfig.getImageSource() != null) {
            Resource imageResource = resourcePatternResolver
                    .getResource("classpath:partners/images/" + partnerConfig.getImageSource());
            try (InputStream inputStream = imageResource.getInputStream()) {
                image = inputStream.readAllBytes();
            }
        }

        partners.add(Partner.builder()
                .name(partnerConfig.getName())
                .description(description)
                .image(image)
                .imageFileName(partnerConfig.getImageSource())
                .build());

        log.info("Partner '{}' added", partnerConfig.getName());
    }

    public String textToSend(Placeholder placeholder) {
        return textToSend.get(placeholder.getValue());
    }

    public String getPartnersList() {
        StringBuilder sb = new StringBuilder("\n");
        int counter = 1;
        for (Partner partner : partners) {
            sb.append("\n")
                    .append(counter)
                    .append(" - ")
                    .append(partner.getName());
            counter++;
        }

        return sb.toString();
    }

    public Partner getPartnerByOption(String option) {
        return partners.get(Integer.parseInt(option) - 1);
    }

    public boolean isValidPartnerOption(String text) {
        try {
            int option = Integer.parseInt(text);
            return option <= partners.size();
        } catch (Exception ex) {
            log.info("Invalid option: {}", text);
            return false;
        }
    }

    public ReplyKeyboardMarkup getPartnersButtons() {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (int counter = 1; counter <= partners.size(); counter++) {
            buttons.add(new KeyboardButton(Integer.toString(counter)));
        }

        return new ReplyKeyboardMarkup(buttons.toArray(KeyboardButton[]::new))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
    }
}
