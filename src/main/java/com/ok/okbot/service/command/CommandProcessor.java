package com.ok.okbot.service.command;

import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommandProcessor {
    private final List<Command> commands;

    public void processMessage(Message message, UserDto user) {
        String commandLabel = Optional.ofNullable(user.getCommand())
                .map(UserCommand::getValue)
                .orElse(message.text());

        commands.stream()
                .filter(c -> c.getCommand().equals(commandLabel))
                .findFirst()
                .ifPresent(c -> c.processMessage(message, user));
    }
}
