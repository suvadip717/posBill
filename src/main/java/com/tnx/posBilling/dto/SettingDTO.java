package com.tnx.posBilling.dto;

import java.util.List;

import lombok.Data;

@Data
public class SettingDTO {
    private long id;
    private String settingKey;
    private String description;
    private String settingType;
    private List<String> options;
    private String defaultValue;
    private String currentValue;
}
