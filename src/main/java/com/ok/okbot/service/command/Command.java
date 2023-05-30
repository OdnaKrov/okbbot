package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;

public interface Command {
    void processMessage(Message message, UserDto user);
    String getCommand();
}
