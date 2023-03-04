package com.ok.okbot.mapper;

import com.ok.okbot.dto.UserDto;
import com.ok.okbot.dto.UserState;
import com.ok.okbot.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityToDtoMapper<UserEntity, UserDto> {

    @Override
    public UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .state(UserState.byLabel(entity.getState()))
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .id(entity.getId())
                .userAgreement(entity.getUserAgreement())
                .build();
    }

    @Override
    public UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .id(dto.getId())
                .lastName(dto.getLastName())
                .state(dto.getState().getLabel())
                .username(dto.getUsername())
                .userAgreement(dto.getUserAgreement())
                .build();
    }
}
