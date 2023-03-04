package com.ok.okbot.mapper;

import com.ok.okbot.dto.DonationDto;
import com.ok.okbot.entity.DonationEntity;
import org.springframework.stereotype.Component;

@Component
public class DonationMapper implements EntityToDtoMapper<DonationEntity, DonationDto> {
    @Override
    public DonationDto toDto(DonationEntity entity) {
        return DonationDto.builder()
                .date(entity.getDate())
                .id(entity.getId())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public DonationEntity toEntity(DonationDto dto) {
        return DonationEntity.builder()
                .date(dto.getDate())
                .userId(dto.getUserId())
                .id(dto.getId())
                .build();
    }
}
