package com.ok.okbot.service.command;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.DonationDto;
import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.mapper.DonationMapper;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.DonationRepository;
import com.ok.okbot.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCommand implements Command {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TelegramBot bot;
    private final MenuConfig menuConfig;
    private final DonationMapper donationMapper;
    private final DonationRepository donationRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void processMessage(Message message, UserDto user) {

        if (user.getCommand() == null) {
            log.info("Notifications command for user: {}", user);
            user.setCommand(UserCommand.NOTIFICATIONS);
            user.setStep(1L);

            userRepository.save(userMapper.toEntity(user));
            bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(Placeholder.NOTIFICATION_DESCRIPTION))
                    .replyMarkup(getNotificationMenuButtons()));
            return;
        }

        if (message.text().equals(OPTION_1)) {
            log.info("Enter notification date for user: {}", user);
            user.setStep(2L);
            userRepository.save(userMapper.toEntity(user));

            bot.execute(new SendMessage(user.getId(), "Введіть дату в форматі дд/мм/рррр"));
            return;
        }

        if (message.text().equals(OPTION_2)) {
            log.info("Show next donation date for user: {}", user);
            user.setCommand(null);
            user.setStep(null);
            userRepository.save(userMapper.toEntity(user));


            Optional<DonationDto> lastDonation = donationRepository.findAllByUserId(user.getId())
                    .stream()
                    .map(donationMapper::toDto)
                    .max(Comparator.comparing(DonationDto::getDate));

            if (lastDonation.isEmpty()) {
                bot.execute(new SendMessage(user.getId(), "У вас ще не будо донацій"));
            } else {
                String msg = String.format("Дата наступної донації: %s",
                        formatter.format(lastDonation.get().getDate().plusDays(60)));
                bot.execute(new SendMessage(user.getId(), msg));
            }
            return;
        }

        if (user.getStep().equals(2L)) {
            log.info("Try to parse date: {} from user: {}", message.text(), user);
            try {
                DonationDto newDonation = DonationDto.builder()
                        .date(LocalDate.parse(message.text(), formatter))
                        .userId(user.getId())
                        .build();

                donationRepository.save(donationMapper.toEntity(newDonation));

                user.setCommand(null);
                user.setStep(null);
                userRepository.save(userMapper.toEntity(user));

                bot.execute(new SendMessage(user.getId(), "Дату донації додано"));
            } catch (Exception ex) {
                log.info("Can't parse {}", message.text(), ex);
                bot.execute(new SendMessage(user.getId(), "незрозумілий формат, спробуйте ще")
                        .replyToMessageId(message.messageId()));
            }
        }
    }

    @Override
    public String getCommand() {
        return UserCommand.NOTIFICATIONS.getValue();
    }

    private ReplyKeyboardMarkup getNotificationMenuButtons() {
        KeyboardButton button1 = new KeyboardButton("1");
        KeyboardButton button2 = new KeyboardButton("2");
        return new ReplyKeyboardMarkup(List.of(button1, button2).toArray(KeyboardButton[]::new))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
    }
}
