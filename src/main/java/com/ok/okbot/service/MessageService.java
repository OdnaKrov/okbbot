package com.ok.okbot.service;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.ProcessorConfig;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.dto.UserState;
import com.ok.okbot.entity.UserEntity;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TelegramBot bot;
    private final MenuConfig menuConfig;

    private final ProcessorConfig processorConfig;

    private static final String USER_AGREEMENT_BUTTON = "Прийняти";
    public void processMessage(Message message) {
        UserDto user = userRepository.findById(message.from().id())
                .map(userMapper::toDto)
                .orElseGet(() -> createNewUser(message.from()));

        if (!user.getUserAgreement()) {
            if (USER_AGREEMENT_BUTTON.equals(message.text())) {
                user.setUserAgreement(true);
                user.setState(UserState.MAIN_MENU);
                userRepository.save(userMapper.toEntity(user));
                bot.execute(new SendMessage(user.getId(),menuConfig.textToSend(UserState.MAIN_MENU))
                        .replyMarkup(menuConfig.getStateKeyboard(user.getState())));
            } else {
                getUserAgreementKeyboard(user);
            }
            return;
        }

        if (message.text() != null) {
            processorConfig.getStateProcessor(user.getState(), message.text()).processMessage(message);
        }
    }

    private void getUserAgreementKeyboard(UserDto user) {
        KeyboardButton button1 = new KeyboardButton(USER_AGREEMENT_BUTTON);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(button1).toArray(KeyboardButton[]::new))
                .resizeKeyboard(true);

        bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(UserState.AGREEMENT_AWAIT))
                .replyMarkup(keyboardMarkup));
    }

    private UserDto createNewUser(User user) {
        return userMapper.toDto(userRepository.save(UserEntity.builder()
                .userAgreement(false)
                .lastName(user.lastName())
                .username(user.username())
                .state(UserState.AGREEMENT_AWAIT.getLabel())
                .firstName(user.firstName())
                .id(user.id())
                .build()));
    }
}
