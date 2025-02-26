package com.tnx.posBilling.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompanySettingDTO {
    private CompanyDTO company;
    private List<SettingDTO> settings;
}
