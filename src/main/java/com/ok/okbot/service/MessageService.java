package com.ok.okbot.service;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.entity.UserEntity;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.UserRepository;
import com.ok.okbot.service.command.CommandProcessor;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
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
    private final BotSenderService sender;
    private final MenuConfig menuConfig;
    private final CommandProcessor commandProcessor;

    private static final String USER_AGREEMENT_BUTTON = "Я погоджуюся";
    public void processMessage(Message message) {
        UserDto user = userRepository.findById(message.from().id())
                .map(userMapper::toDto)
                .orElseGet(() -> createNewUser(message.from()));

        // get user agreement
        if (!user.getUserAgreement()) {
            if (USER_AGREEMENT_BUTTON.equals(message.text())) {
                user.setUserAgreement(true);
                userRepository.save(userMapper.toEntity(user));
                sender.sendShareContactButton(user.getId(), menuConfig.textToSend(Placeholder.SHARE_CONTACT));
            } else {
                getUserAgreementKeyboard(user);
            }
            return;
        }

        // set user contact
        if (message.contact() != null) {
            user.setPhoneNumber(message.contact().phoneNumber());
            user.setFirstName(message.contact().firstName());
            user.setLastName(message.contact().lastName());

            userRepository.save(userMapper.toEntity(user));

            sender.sendReplyKeyboardMessage(user.getId(), menuConfig.textToSend(Placeholder.MAIN_MENU), new ReplyKeyboardRemove());
            return;
        }

        // force to get user contact if empty
        if (user.getPhoneNumber() == null) {
            sender.sendShareContactButton(user.getId(), menuConfig.textToSend(Placeholder.SHARE_CONTACT));
            return;
        }

        if (message.text() != null) {
            log.info("process message");
            commandProcessor.processMessage(message, user);
        }
    }

    private void getUserAgreementKeyboard(UserDto user) {
        KeyboardButton button1 = new KeyboardButton(USER_AGREEMENT_BUTTON);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(button1).toArray(KeyboardButton[]::new))
                .resizeKeyboard(true);

        sender.sendReplyKeyboardMessage(user.getId(), menuConfig.textToSend(Placeholder.AGREEMENT_AWAIT), keyboardMarkup);
    }

    private UserDto createNewUser(User user) {
        return userMapper.toDto(userRepository.save(UserEntity.builder()
                .userAgreement(false)
                .lastName(user.lastName())
                .username(user.username())
                .firstName(user.firstName())
                .id(user.id())
                .build()));
    }
}
