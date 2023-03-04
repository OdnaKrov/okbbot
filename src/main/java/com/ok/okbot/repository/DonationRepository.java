package com.ok.okbot.repository;

import com.ok.okbot.entity.DonationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends CrudRepository<DonationEntity, Long> {
    List<DonationEntity> findAllByUserId(Long userId);
}
