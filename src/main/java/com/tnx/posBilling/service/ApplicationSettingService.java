package com.tnx.posBilling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.ApplicationSetting;
import com.tnx.posBilling.repository.ApplicationSettingRepository;

@Service
public class ApplicationSettingService {

    @Autowired
    private ApplicationSettingRepository settingRepository;

    public ApplicationSetting saveSetting(ApplicationSetting setting) {
        return settingRepository.save(setting);
    }

    public List<ApplicationSetting> getAllSettings() {
        return settingRepository.findAll();
    }

    public ApplicationSetting getSettingById(Long id) {
        return settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application Setting is not found"));
    }

    public ApplicationSetting updateSetting(Long id, ApplicationSetting updatedSetting) {
        return settingRepository.findById(id).map(setting -> {
            setting.setSettingKey(updatedSetting.getSettingKey());
            setting.setDescription(updatedSetting.getDescription());
            setting.setSettingType(updatedSetting.getSettingType());
            setting.setOptions(updatedSetting.getOptions());
            setting.setDefaultValue(updatedSetting.getDefaultValue());
            return settingRepository.save(setting);
        }).orElseThrow(() -> new ResourceNotFoundException("Setting not found"));
    }

    public void deleteSetting(Long id) {
        settingRepository.deleteById(id);
    }
}
