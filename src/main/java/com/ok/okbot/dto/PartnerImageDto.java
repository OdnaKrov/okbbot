package com.ok.okbot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerImageDto {

    private Long id;
    private String fileName;
    private String fileId;
    private Integer checksum;
}
