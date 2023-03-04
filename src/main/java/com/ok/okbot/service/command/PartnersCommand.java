package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartnersCommand implements Command {

    private final PartnersService partnersService;

    @Override
    public void processMessage(Message message, UserDto user) {
        partnersService.processMessage(message, user);
    }

    @Override
    public String getCommand() {
        return UserCommand.PARTNER_BONUS.getValue();
    }
}
