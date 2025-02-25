package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.CompanySettings;
import com.tnx.posBilling.service.CompanySettingService;

@RestController
@RequestMapping("/company-settings")
public class CompanySettingController {
    @Autowired
    private CompanySettingService companySettingsService;

    @PostMapping
    public ResponseEntity<CompanySettings> createCompanySetting(@RequestBody CompanySettings companySettings) {
        return ResponseEntity.ok(companySettingsService.saveCompanySetting(companySettings));
    }

    @GetMapping
    public ResponseEntity<List<CompanySettings>> getAllCompanySettings() {
        return ResponseEntity.ok(companySettingsService.getAllCompanySettings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanySettings> getCompanySettingById(@PathVariable Long id) {
        CompanySettings setting = companySettingsService.getCompanySettingById(id);
        return ResponseEntity.ok(setting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanySettings> updateCompanySetting(@PathVariable Long id,
            @RequestBody CompanySettings updatedSetting) {
        return ResponseEntity.ok(companySettingsService.updateCompanySetting(id, updatedSetting));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanySetting(@PathVariable Long id) {
        companySettingsService.deleteCompanySetting(id);
        return ResponseEntity.noContent().build();
    }
}
