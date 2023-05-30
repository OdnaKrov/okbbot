package com.ok.okbot.repository;

import com.ok.okbot.entity.ImageContentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageContentRepository extends CrudRepository<ImageContentEntity, Long> {
    Optional<ImageContentEntity> findByFileNameAndChecksum(String fileName, Integer checksum);
}
