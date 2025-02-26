package com.tnx.posBilling.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.ApplicationSetting;
import com.tnx.posBilling.repository.ApplicationSettingRepository;
import com.tnx.posBilling.service.interfaces.ApplicationSettingService;

@Service
public class ApplicationSettingServiceImpl implements ApplicationSettingService {
    @Autowired
    private ApplicationSettingRepository settingRepository;

    public ResponseEntity<ApplicationSetting> saveSetting(ApplicationSetting setting) {
        return new ResponseEntity<>(settingRepository.save(setting), HttpStatus.CREATED);
    }

    public ResponseEntity<List<ApplicationSetting>> getAllSettings() {
        return ResponseEntity.ok(settingRepository.findAll());
    }

    public ResponseEntity<ApplicationSetting> getSettingById(Long id) {
        ApplicationSetting applicationSetting = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application Setting is not found"));
        return ResponseEntity.ok(applicationSetting);
    }

    public ResponseEntity<ApplicationSetting> updateSetting(Long id, ApplicationSetting updatedSetting) {
        ApplicationSetting applicationSetting = settingRepository.findById(id).map(setting -> {
            setting.setSettingKey(updatedSetting.getSettingKey());
            setting.setDescription(updatedSetting.getDescription());
            setting.setSettingType(updatedSetting.getSettingType());
            setting.setOptions(updatedSetting.getOptions());
            setting.setDefaultValue(updatedSetting.getDefaultValue());
            // setting.setCurrentValue(updatedSetting.getCurrentValue());
            return settingRepository.save(setting);
        }).orElseThrow(() -> new ResourceNotFoundException("Setting not found"));
        return ResponseEntity.ok(applicationSetting);
    }

    public ResponseEntity<String> deleteSetting(Long id) {
        ApplicationSetting setting = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Setting is not found"));
        if (setting != null) {
            settingRepository.deleteById(id);
        }
        return ResponseEntity.ok("Setting Deleted");
    }
}
