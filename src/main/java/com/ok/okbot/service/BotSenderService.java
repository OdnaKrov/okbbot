package com.ok.okbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotSenderService {

    private final TelegramBot bot;

    public void sendShareContactButton(Long chatId, String message) {
        bot.execute(new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton("Поділитися контактом")
                        .requestContact(true))
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)));
    }

    public SendResponse sendPhoto(Long chatId, byte[] file, String message) {
        var resp = bot.execute(new SendPhoto(chatId, file)
                .caption(message)
                .parseMode(ParseMode.HTML));
        if (!resp.isOk()) {
            log.info("Error from telegram: {}", resp.message());
        }

        return resp;
    }
    public void sendPhoto(Long chatId, String fileId, String message) {
        var resp = bot.execute(new SendPhoto(chatId, fileId)
                .caption(message)
                .parseMode(ParseMode.HTML));
        if (!resp.isOk()) {
            log.info("Error from telegram: {}", resp.message());
        }
    }
    public void sendMessage(Long chatId, String message, Integer replyMessage) {
        var resp = bot.execute(new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .replyToMessageId(replyMessage));
        if (!resp.isOk()) {
            log.info("Error from telegram: {}", resp.message());
        }
    }
    public void sendMessage(Long chatId, String message) {
        var resp = bot.execute(new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML));
        if (!resp.isOk()) {
            log.info("Error from telegram: {}", resp.message());
        }
    }

    public void sendReplyKeyboardMessage(Long chatId, String message, Keyboard keyboard) {
        var resp = bot.execute(new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .replyMarkup(keyboard));
        if (!resp.isOk()) {
            log.info("Error from telegram: {}", resp.message());
        }
    }
}
