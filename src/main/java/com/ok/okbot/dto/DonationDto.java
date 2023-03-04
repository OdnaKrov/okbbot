package com.ok.okbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DonationDto {
    private Long id;
    private Long userId;
    private LocalDate date;
}
