package com.ok.okbot.service;

import com.ok.okbot.dto.UserState;
import com.pengrad.telegrambot.model.Message;

public interface StateProcessor {

    void processMessage(Message message);

    UserState getState();

    String getOption();
}
