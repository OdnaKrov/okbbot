package com.ok.okbot.service;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.dto.UserState;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhatCanWeDoBackToMenuProcessor implements StateProcessor {

    private final TelegramBot bot;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final MenuConfig menuConfig;

    @Override
    public void processMessage(Message message) {
        UserDto user = userRepository.findById(message.from().id())
                .map(userMapper::toDto)
                .orElseThrow();

        user.setState(UserState.MAIN_MENU);
        userRepository.save(userMapper.toEntity(user));
        bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(UserState.MAIN_MENU))
                .replyMarkup(menuConfig.getStateKeyboard(user.getState())));
    }

    @Override
    public UserState getState() {
        return UserState.WHAT_CAN_WE_DO;
    }

    @Override
    public String getOption() {
        return "1";
    }
}
