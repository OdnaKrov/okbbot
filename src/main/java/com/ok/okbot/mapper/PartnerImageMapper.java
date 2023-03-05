package com.ok.okbot.mapper;

import com.ok.okbot.dto.PartnerImageDto;
import com.ok.okbot.entity.PartnerImageEntity;
import org.springframework.stereotype.Component;

@Component
public class PartnerImageMapper implements EntityToDtoMapper<PartnerImageEntity, PartnerImageDto> {
    @Override
    public PartnerImageDto toDto(PartnerImageEntity entity) {
        return PartnerImageDto.builder()
                .fileName(entity.getFileName())
                .checksum(entity.getChecksum())
                .id(entity.getId())
                .fileId(entity.getFileId())
                .build();
    }

    @Override
    public PartnerImageEntity toEntity(PartnerImageDto dto) {
        return PartnerImageEntity.builder()
                .checksum(dto.getChecksum())
                .fileId(dto.getFileId())
                .fileName(dto.getFileName())
                .id(dto.getId())
                .build();
    }
}
