package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.dto.CompanySettingDTO;

import com.tnx.posBilling.model.CompanySettings;

public interface CompanySettingService {

    public ResponseEntity<CompanySettings> saveCompanySetting(CompanySettings companySettings);

    public ResponseEntity<CompanySettings> updateCompanySetting(Long companyId, Long settingId,
            String currentValue);

    public ResponseEntity<CompanySettingDTO> getAllSettingsForCompany(Long companyId);

    public ResponseEntity<List<CompanySettingDTO>> getAllCompanySettings();
}
