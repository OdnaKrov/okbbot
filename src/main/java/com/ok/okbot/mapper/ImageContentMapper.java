package com.ok.okbot.mapper;

import com.ok.okbot.dto.ImageContentDto;
import com.ok.okbot.entity.ImageContentEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageContentMapper implements EntityToDtoMapper<ImageContentEntity, ImageContentDto> {
    @Override
    public ImageContentDto toDto(ImageContentEntity entity) {
        return ImageContentDto.builder()
                .fileName(entity.getFileName())
                .checksum(entity.getChecksum())
                .id(entity.getId())
                .fileId(entity.getFileId())
                .build();
    }

    @Override
    public ImageContentEntity toEntity(ImageContentDto dto) {
        return ImageContentEntity.builder()
                .checksum(dto.getChecksum())
                .fileId(dto.getFileId())
                .fileName(dto.getFileName())
                .id(dto.getId())
                .build();
    }
}
