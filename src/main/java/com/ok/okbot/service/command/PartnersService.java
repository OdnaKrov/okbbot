package com.ok.okbot.service.command;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.Partner;
import com.ok.okbot.dto.PartnerImageDto;
import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.mapper.PartnerImageMapper;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.PartnerImageRepository;
import com.ok.okbot.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TelegramBot bot;
    private final MenuConfig menuConfig;
    private final PartnerImageRepository partnerImageRepository;
    private final PartnerImageMapper partnerImageMapper;

    public void processMessage(Message message, UserDto user) {
        if (user.getCommand() == null) {
            log.info("Partners command for: {}", user);
            user.setCommand(UserCommand.PARTNER_BONUS);
            user.setStep(1L);

            userRepository.save(userMapper.toEntity(user));
            bot.execute(new SendMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.PARTNERS_DESCRIPTION) + menuConfig.getPartnersList())
                    .replyMarkup(menuConfig.getPartnersButtons()));
            return;
        }

        if (menuConfig.isValidPartnerOption(message.text())) {
            log.info("Partners menu option: {} for user: {}", message.text(), user);
            user.setCommand(null);
            user.setStep(null);

            userRepository.save(userMapper.toEntity(user));

            Partner partner = menuConfig.getPartnerByOption(message.text());

            if (partner.getImage() != null) {
                Optional<PartnerImageDto> cacheImage =
                        partnerImageRepository.findByFileNameAndChecksum(partner.getImageFileName(), checkSum(partner.getImage()))
                                .map(partnerImageMapper::toDto);

                if (cacheImage.isPresent()) {
                    bot.execute(new SendPhoto(user.getId(), cacheImage.get().getFileId())
                            .caption(partner.getDescription()));
                    log.info("Using image cache for: {}", cacheImage.get().getFileId());
                } else {
                    var resp = bot.execute(new SendPhoto(user.getId(), partner.getImage())
                            .caption(partner.getDescription()));
                    partnerImageRepository.save(partnerImageMapper.toEntity(
                            PartnerImageDto.builder()
                                    .fileId(resp.message().photo()[0].fileId())
                                    .fileName(partner.getImageFileName())
                                    .checksum(checkSum(partner.getImage()))
                                    .build()
                    ));
                    log.info("New cache image added for {}", partner.getImageFileName());
                }
            } else {
                bot.execute(new SendMessage(user.getId(), partner.getDescription()));
            }

            bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(Placeholder.GRATITUDE)));
            bot.execute(new SendMessage(user.getId(), menuConfig.textToSend(Placeholder.CONTACTS)));
        } else {
            bot.execute(new SendMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.PARTNERS_DESCRIPTION) + menuConfig.getPartnersList())
                    .replyMarkup(menuConfig.getPartnersButtons()));
            log.info("Invalid option for partners: {} from {}", message.text(), user);
        }
    }

    public static int checkSum(byte[] array) {
        int result = 0;
        for (final byte v : array) {
            result += v;
        }
        return result;
    }
}
