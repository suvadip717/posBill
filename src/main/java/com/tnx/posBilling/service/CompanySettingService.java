package com.tnx.posBilling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.CompanySettings;
import com.tnx.posBilling.repository.CompanySettingsRepository;

@Service
public class CompanySettingService {
    @Autowired
    private CompanySettingsRepository companySettingsRepository;

    public CompanySettings saveCompanySetting(CompanySettings companySettings) {
        return companySettingsRepository.save(companySettings);
    }

    public List<CompanySettings> getAllCompanySettings() {
        return companySettingsRepository.findAll();
    }

    public CompanySettings getCompanySettingById(Long id) {
        return companySettingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Setting is not found"));
    }

    public CompanySettings updateCompanySetting(Long id, CompanySettings updatedSetting) {
        return companySettingsRepository.findById(id).map(setting -> {
            setting.setCompany(updatedSetting.getCompany());
            setting.setSetting(updatedSetting.getSetting());
            setting.setCurrentValue(updatedSetting.getCurrentValue());
            return companySettingsRepository.save(setting);
        }).orElseThrow(() -> new ResourceNotFoundException("Company setting not found"));
    }

    public void deleteCompanySetting(Long id) {
        companySettingsRepository.deleteById(id);
    }
}
