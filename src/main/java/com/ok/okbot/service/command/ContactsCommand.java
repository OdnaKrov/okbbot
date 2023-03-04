package com.ok.okbot.service.command;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactsCommand implements Command {

    private final MenuConfig menuConfig;
    private final TelegramBot bot;

    @Override
    public void processMessage(Message message, UserDto user) {
        log.info("Contacts command for user: {}", user);
        bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(Placeholder.CONTACTS)));
    }

    @Override
    public String getCommand() {
        return UserCommand.CONTACTS.getValue();
    }
}
