package com.tnx.posBilling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.dto.CompanySettingDTO;
import com.tnx.posBilling.model.CompanySettings;
import com.tnx.posBilling.service.impl.CompanySettingServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/api/company")
public class CompanySettingController {
    @Autowired
    private CompanySettingServiceImpl companySettingsService;

    @GetMapping
    public ResponseEntity<List<CompanySettingDTO>> getAllCompanySettings() {
        return companySettingsService.getAllCompanySettings();
    }

    @PutMapping("/{companyId}/settings/{settingId}")
    public ResponseEntity<CompanySettings> updateCompanySetting(
            @PathVariable Long companyId,
            @PathVariable Long settingId,
            @RequestBody Map<String, String> requestBody) {

        String currentValue = requestBody.get("currentValue");

        return companySettingsService.updateCompanySetting(companyId, settingId,
                currentValue);
    }

    @GetMapping("/settings/{companyId}")
    public ResponseEntity<CompanySettingDTO> getCompanySettings(@PathVariable Long companyId) {
        return companySettingsService.getAllSettingsForCompany(companyId);
    }
}
