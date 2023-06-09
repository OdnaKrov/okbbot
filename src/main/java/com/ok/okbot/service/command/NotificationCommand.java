package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCommand implements Command {

    private final NotificationService notificationService;

    @Override
    public void processMessage(Message message, UserDto user) {
        notificationService.processMessage(message, user);
    }

    @Override
    public String getCommand() {
        return UserCommand.NOTIFICATIONS.getValue();
    }
}
