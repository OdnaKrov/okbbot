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
public class QACommand implements Command {
    private final MenuConfig menuConfig;
    private final BotSenderService sender;
    @Override
    public void processMessage(Message message, UserDto user) {
        log.info("QA command for user: {}", user);
        sender.sendMessage(user.getId(), menuConfig.textToSend(Placeholder.QA));
    }

    @Override
    public String getCommand() {
        return UserCommand.QA.getValue();
    }
}
