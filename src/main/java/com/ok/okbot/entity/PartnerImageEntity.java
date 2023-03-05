package com.ok.okbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity(name = "partner_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerImageEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "check_sum")
    private Integer checksum;
}
