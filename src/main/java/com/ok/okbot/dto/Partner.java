package com.ok.okbot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Partner {
    private String name;
    private byte[] image;
    private String description;
    private String imageFileName;
}
