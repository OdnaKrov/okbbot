package com.ok.okbot.mapper;

public interface EntityToDtoMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
