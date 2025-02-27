package com.tnx.posBilling.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.dto.CompanySettingDTO;
import com.tnx.posBilling.dto.SettingDTO;
import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.ApplicationSetting;
import com.tnx.posBilling.model.Company;
import com.tnx.posBilling.model.CompanySettings;
import com.tnx.posBilling.repository.ApplicationSettingRepository;
import com.tnx.posBilling.repository.CompanyRepository;
import com.tnx.posBilling.repository.CompanySettingsRepository;
import com.tnx.posBilling.service.interfaces.CompanySettingService;
import com.tnx.posBilling.utils.Utils;

@Service
public class CompanySettingServiceImpl implements CompanySettingService {
    @Autowired
    private CompanySettingsRepository companySettingsRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationSettingRepository aRepository;

    @Override
    public ResponseEntity<CompanySettings> saveCompanySetting(CompanySettings companySettings) {
        return new ResponseEntity<>(companySettingsRepository.save(companySettings), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CompanySettings> updateCompanySetting(Long companyId, Long settingId,
            String currentValue) {
        // Find company
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        // Find application setting
        ApplicationSetting appSetting = aRepository.findById(settingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Application setting not found with key: "));

        if (currentValue == null) {
            throw new ResourceNotFoundException("Please enter current value");
        }
        // Find existing company setting or create a new one
        CompanySettings companySettings = companySettingsRepository
                .findByCompanyIdAndSettingId(companyId, settingId)
                .orElse(new CompanySettings());

        companySettings.setCompany(company);
        companySettings.setSetting(appSetting);
        companySettings.setCurrentValue(currentValue);

        return ResponseEntity.ok(companySettingsRepository.save(companySettings));
    }

    @Override
    public ResponseEntity<CompanySettingDTO> getAllSettingsForCompany(Long companyId) {
        List<ApplicationSetting> allSettings = aRepository.findAll();
        List<CompanySettings> companySettings = companySettingsRepository.findByCompanyId(companyId);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        // Map company settings for quick lookup
        Map<Long, CompanySettings> companySettingsMap = companySettings.stream()
                .collect(Collectors.toMap(cs -> cs.getSetting().getId(), cs -> cs));

        // Merge application settings with company settings
        List<SettingDTO> settingDTOs = allSettings.stream()
                .map(setting -> {
                    CompanySettings companySetting = companySettingsMap.get(setting.getId());
                    // String currentValue = (companySetting != null) ?
                    // companySetting.getCurrentValue()
                    // : setting.getDefaultValue();
                    String currentValue = (companySetting != null) ? companySetting.getCurrentValue()
                            : null;

                    return Utils.mapSettingDTOtoApplicationSetting(setting, currentValue);
                })
                .collect(Collectors.toList());

        CompanySettingDTO companySettingDTO = Utils
                .mapCompanySettingDTOtoCompanySetting(Utils.mapCompanyDTOtoCompany(company), settingDTOs);
        return ResponseEntity.ok(companySettingDTO);
    }

    @Override
    public ResponseEntity<List<CompanySettingDTO>> getAllCompanySettings() {
        List<Company> companies = companyRepository.findAll();

        List<CompanySettingDTO> companySettingDTOs = companies.stream().map(company -> {
            List<CompanySettings> settings = companySettingsRepository.findByCompanyId(company.getId());

            List<SettingDTO> settingDTOs = settings.stream()
                    .map(setting -> Utils.mapSettingDTOtoApplicationSetting(setting.getSetting(),
                            setting.getCurrentValue()))
                    .collect(Collectors.toList());

            return Utils.mapCompanySettingDTOtoCompanySetting(Utils.mapCompanyDTOtoCompany(company), settingDTOs);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(companySettingDTOs);
    }

}
