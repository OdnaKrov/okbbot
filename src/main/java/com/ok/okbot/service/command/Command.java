package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;

public interface Command {

    String OPTION_1 = "Додати дату донації";
    String OPTION_2 = "Подивитися дату донації";
    void processMessage(Message message, UserDto user);
    String getCommand();
}
