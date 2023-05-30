package com.ok.okbot.conf;

import com.google.gson.Gson;
import com.ok.okbot.dto.ImageContent;
import com.ok.okbot.dto.ImageContentConfig;
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
    private final List<ImageContent> partners = new ArrayList<>();
    private final List<ImageContent> qa = new ArrayList<>();

    public static final String OPTION_1 = "Додати дату донації";
    public static final String OPTION_2 = "Подивитися дату донації";
    public static final String BACK_BUTTON = "Назад";

    public static final int LINE_SIZE = 5;

    public MenuConfig(ResourcePatternResolver resourcePatternResolver) throws IOException {
        this.resourcePatternResolver = resourcePatternResolver;
        configureMenuText();
        loadPartners();
        loadQA();
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
                ImageContentConfig partnerConfig = gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), ImageContentConfig.class);
                processPartnerConfig(partnerConfig);
            }
        }
    }

    private void loadQA() throws IOException {
        log.info("Load FAQ");
        Resource[] resources = resourcePatternResolver.getResources("classpath*:qa/*.json");
        Gson gson = new Gson();
        for (Resource resource : resources) {
            log.info("Process qa file: {}", resource.getFilename());
            try (InputStream inputStream = resource.getInputStream()) {
                ImageContentConfig partnerConfig = gson.fromJson(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), ImageContentConfig.class);
                processQaConfig(partnerConfig);
            }
        }
    }

    private void processImageConfig(ImageContentConfig partnerConfig, List<ImageContent> imageContent, String prefix) throws IOException {
        Resource descriptionResource = resourcePatternResolver
                .getResource(prefix + "/description/" + partnerConfig.getDescriptionSource());

        String description;
        try (InputStream inputStream = descriptionResource.getInputStream()) {
            description = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        byte[] image = null;
        if (partnerConfig.getImageSource() != null) {
            Resource imageResource = resourcePatternResolver
                    .getResource(prefix + "/images/" + partnerConfig.getImageSource());
            try (InputStream inputStream = imageResource.getInputStream()) {
                image = inputStream.readAllBytes();
            }
        }

        imageContent.add(ImageContent.builder()
                .name(partnerConfig.getName())
                .description(description)
                .image(image)
                .imageFileName(partnerConfig.getImageSource())
                .build());

        log.info("content '{}' added", partnerConfig.getName());
    }

    private void processPartnerConfig(ImageContentConfig partnerConfig) throws IOException {
        processImageConfig(partnerConfig, partners, "classpath:partners");
    }

    private void processQaConfig(ImageContentConfig qaConfig) throws IOException {
        processImageConfig(qaConfig, qa, "classpath:qa");
    }

    public String textToSend(Placeholder placeholder) {
        return textToSend.get(placeholder.getValue());
    }

    public String getPartnersList() {
        StringBuilder sb = new StringBuilder("\n");
        int counter = 1;
        for (ImageContent partner : partners) {
            sb.append("\n")
                    .append(counter)
                    .append(" - ")
                    .append(partner.getName());
            counter++;
        }

        return sb.toString();
    }

    public String getQAList() {
        StringBuilder sb = new StringBuilder("\n");
        int counter = 1;
        for (ImageContent content : qa) {
            sb.append("\n")
                    .append(counter)
                    .append(" - ")
                    .append(content.getName());
            counter++;
        }

        return sb.toString();
    }

    public ImageContent getPartnerByOption(String option) {
        return partners.get(Integer.parseInt(option) - 1);
    }

    public ImageContent getQaByOption(String option) {
        return qa.get(Integer.parseInt(option) - 1);
    }

    public boolean isValidPartnerOption(String text) {
        try {
            if (BACK_BUTTON.equals(text)) {
                return true;
            }
            int option = Integer.parseInt(text);
            return option <= partners.size();
        } catch (Exception ex) {
            log.info("Invalid option: {}", text);
            return false;
        }
    }

    public boolean isValidQaOption(String text) {
        try {
            if (BACK_BUTTON.equals(text)) {
                return true;
            }
            int option = Integer.parseInt(text);
            return option <= qa.size();
        } catch (Exception ex) {
            log.info("Invalid option: {}", text);
            return false;
        }
    }

    public ReplyKeyboardMarkup getPartnersButtons() {
        if (partners.size() + 1 <= LINE_SIZE) {
            return getOneLinePartnersButtons(partners);
        }
        return getMultyLinePartnersButtons(partners);
    }

    public ReplyKeyboardMarkup getQaButtons() {
        if (qa.size() + 1 <= LINE_SIZE) {
            return getOneLinePartnersButtons(qa);
        }
        return getMultyLinePartnersButtons(qa);
    }

    public ReplyKeyboardMarkup getMultyLinePartnersButtons(List<ImageContent> content) {
        int linesCount = content.size() % LINE_SIZE > 0 ?
                (partners.size() / LINE_SIZE) + 1 : content.size() / LINE_SIZE;
        KeyboardButton[][] multyLineButtons = new KeyboardButton[linesCount][];
        List<KeyboardButton> buttons = new ArrayList<>();

        int buttonCounter = 0;
        int lineIndex = 0;
        for (int counter = 1; counter <= content.size() + 1; counter++) {
            buttonCounter++;
            buttons.add(counter == content.size() + 1 ? new KeyboardButton(BACK_BUTTON) : new KeyboardButton(Integer.toString(counter)));
            if (buttonCounter == LINE_SIZE) {
                multyLineButtons[lineIndex] = buttons.toArray(KeyboardButton[]::new);
                buttons = new ArrayList<>();
                lineIndex++;
                buttonCounter = 0;
            }
        }

        if (lineIndex < multyLineButtons.length) {
            multyLineButtons[lineIndex] = buttons.toArray(KeyboardButton[]::new);
        }
        return new ReplyKeyboardMarkup(multyLineButtons)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
    }

    public ReplyKeyboardMarkup getOneLinePartnersButtons(List<ImageContent> content) {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (int counter = 1; counter <= content.size(); counter++) {
            buttons.add(new KeyboardButton(Integer.toString(counter)));
        }
        buttons.add(new KeyboardButton(BACK_BUTTON));

        return new ReplyKeyboardMarkup(buttons.toArray(KeyboardButton[]::new))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
    }
}
