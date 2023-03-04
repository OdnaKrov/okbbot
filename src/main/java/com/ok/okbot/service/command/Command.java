package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;

public interface Command {

    String OPTION_1 = "1";
    String OPTION_2 = "2";
    void processMessage(Message message, UserDto user);
    String getCommand();
}
