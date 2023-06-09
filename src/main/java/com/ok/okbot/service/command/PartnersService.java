package com.ok.okbot.service.command;

import com.ok.okbot.conf.MenuConfig;
import com.ok.okbot.conf.Placeholder;
import com.ok.okbot.dto.ImageContent;
import com.ok.okbot.dto.ImageContentDto;
import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.mapper.ImageContentMapper;
import com.ok.okbot.mapper.UserMapper;
import com.ok.okbot.repository.ImageContentRepository;
import com.ok.okbot.repository.UserRepository;
import com.ok.okbot.service.BotSenderService;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ok.okbot.conf.MenuConfig.BACK_BUTTON;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnersService implements ImageContentService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BotSenderService sender;
    private final MenuConfig menuConfig;
    private final ImageContentRepository imageContentRepository;
    private final ImageContentMapper imageContentMapper;

    public void processMessage(Message message, UserDto user) {
        if (user.getCommand() == null) {
            log.info("Partners command for: {}", user);
            user.setCommand(UserCommand.PARTNER_BONUS);
            user.setStep(1L);

            userRepository.save(userMapper.toEntity(user));
            sender.sendReplyKeyboardMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.PARTNERS_DESCRIPTION) + menuConfig.getPartnersList(),
                    menuConfig.getPartnersButtons());
            return;
        }

        if (menuConfig.isValidPartnerOption(message.text())) {
            log.info("Partners menu option: {} for user: {}", message.text(), user);
            user.setCommand(null);
            user.setStep(null);

            userRepository.save(userMapper.toEntity(user));

            if (BACK_BUTTON.equals(message.text())) {
                log.info("Back from partners for user: {}", user);
                return;
            }

            ImageContent partner = menuConfig.getPartnerByOption(message.text());

            if (partner.getImage() != null) {
                Optional<ImageContentDto> cacheImage =
                        imageContentRepository.findByFileNameAndChecksum(partner.getImageFileName(), checkSum(partner.getImage()))
                                .map(imageContentMapper::toDto);

                if (cacheImage.isPresent()) {
                    sender.sendPhoto(user.getId(), cacheImage.get().getFileId(), partner.getDescription());
                    log.info("Using image cache for: {}", cacheImage.get().getFileId());
                } else {
                    var resp = sender.sendPhoto(user.getId(), partner.getImage(), partner.getDescription());
                    imageContentRepository.save(imageContentMapper.toEntity(
                            ImageContentDto.builder()
                                    .fileId(resp.message().photo()[0].fileId())
                                    .fileName(partner.getImageFileName())
                                    .checksum(checkSum(partner.getImage()))
                                    .build()
                    ));
                    log.info("New cache image added for {}", partner.getImageFileName());
                }
            } else {
                sender.sendMessage(user.getId(), partner.getDescription());
            }

        } else {
            sender.sendReplyKeyboardMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.PARTNERS_DESCRIPTION) + menuConfig.getPartnersList(),
                    menuConfig.getPartnersButtons());
            log.info("Invalid option for partners: {} from {}", message.text(), user);
        }
    }
}
