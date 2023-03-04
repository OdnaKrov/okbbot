package com.ok.okbot.mapper;

import com.ok.okbot.dto.UserCommand;
import com.ok.okbot.dto.UserDto;
import com.ok.okbot.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements EntityToDtoMapper<UserEntity, UserDto> {

    @Override
    public UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .command(UserCommand.byValue(entity.getCommand()))
                .step(entity.getStep())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .id(entity.getId())
                .userAgreement(entity.getUserAgreement())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    @Override
    public UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .id(dto.getId())
                .lastName(dto.getLastName())
                .command(Optional.ofNullable(dto.getCommand())
                        .map(UserCommand::getValue)
                        .orElse(null))
                .step(dto.getStep())
                .username(dto.getUsername())
                .userAgreement(dto.getUserAgreement())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
