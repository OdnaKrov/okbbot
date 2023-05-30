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
public class QAService implements ImageContentService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BotSenderService sender;
    private final MenuConfig menuConfig;
    private final ImageContentRepository imageContentRepository;
    private final ImageContentMapper imageContentMapper;

    public void processMessage(Message message, UserDto user) {
        if (user.getCommand() == null) {
            log.info("QA command for: {}", user);
            user.setCommand(UserCommand.QA);
            user.setStep(1L);

            userRepository.save(userMapper.toEntity(user));
            sender.sendReplyKeyboardMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.QA) + menuConfig.getQAList(),
                    menuConfig.getQaButtons());
            return;
        }

        if (menuConfig.isValidQaOption(message.text())) {
            log.info("QA menu option: {} for user: {}", message.text(), user);
            user.setCommand(null);
            user.setStep(null);

            userRepository.save(userMapper.toEntity(user));

            if (BACK_BUTTON.equals(message.text())) {
                log.info("Back from qa for user: {}", user);
                return;
            }

            ImageContent qa = menuConfig.getQaByOption(message.text());

            if (qa.getImage() != null) {
                Optional<ImageContentDto> cacheImage =
                        imageContentRepository.findByFileNameAndChecksum(qa.getImageFileName(), checkSum(qa.getImage()))
                                .map(imageContentMapper::toDto);

                if (cacheImage.isPresent()) {
                    sender.sendPhoto(user.getId(), cacheImage.get().getFileId(), qa.getDescription());
                    log.info("Using image cache for: {}", cacheImage.get().getFileId());
                } else {
                    var resp = sender.sendPhoto(user.getId(), qa.getImage(), qa.getDescription());
                    imageContentRepository.save(imageContentMapper.toEntity(
                            ImageContentDto.builder()
                                    .fileId(resp.message().photo()[0].fileId())
                                    .fileName(qa.getImageFileName())
                                    .checksum(checkSum(qa.getImage()))
                                    .build()
                    ));
                    log.info("New cache image added for {}", qa.getImageFileName());
                }
            } else {
                sender.sendMessage(user.getId(), qa.getDescription());
            }

        } else {
            sender.sendReplyKeyboardMessage(user.getId(),
                    menuConfig.textToSend(Placeholder.QA) + menuConfig.getQAList(),
                    menuConfig.getQaButtons());
            log.info("Invalid option for QA: {} from {}", message.text(), user);
        }
    }
}
