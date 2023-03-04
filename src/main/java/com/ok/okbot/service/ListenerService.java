package com.ok.okbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListenerService {

    private final MessageService messageService;
    private final TelegramBot bot;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            this.processUpdates(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void processUpdates(List<Update> updates) {
      log.info("Update {}", updates);
      updates.forEach(update -> {

          if (update.message() != null && Chat.Type.Private.equals(update.message().chat().type())) {
              messageService.processMessage(update.message());
          }
      });
    }
}
