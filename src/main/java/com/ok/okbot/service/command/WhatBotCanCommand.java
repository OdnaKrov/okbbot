package com.ok.okbot.service.command;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.service.BotSenderService;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhatBotCanCommand implements Command {
    private final MenuConfig menuConfig;
    private final BotSenderService sender;
    private final NotificationService notificationService;

    @Override
    public void processMessage(Message message, UserDto user) {
        log.info("What can bot do command for user: {}", user);
        sender.sendMessage(user.getId(), menuConfig.textToSend(Placeholder.WHAT_WE_CAN_DO));
        notificationService.processMessage(message, user);
    }

    @Override
    public String getCommand() {
        return UserCommand.WHAT_BOT_CAN.getValue();
    }
}
