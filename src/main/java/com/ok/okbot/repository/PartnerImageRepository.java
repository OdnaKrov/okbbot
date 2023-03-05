package com.ok.okbot.repository;

import com.ok.okbot.entity.PartnerImageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerImageRepository extends CrudRepository<PartnerImageEntity, Long> {
    Optional<PartnerImageEntity> findByFileNameAndChecksum(String fileName, Integer checksum);
}
