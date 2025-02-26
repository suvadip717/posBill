package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.ApplicationSetting;

public interface ApplicationSettingService {

    public ResponseEntity<ApplicationSetting> saveSetting(ApplicationSetting setting);

    public ResponseEntity<List<ApplicationSetting>> getAllSettings();

    public ResponseEntity<ApplicationSetting> getSettingById(Long id);

    public ResponseEntity<ApplicationSetting> updateSetting(Long id, ApplicationSetting updatedSetting);

    public ResponseEntity<String> deleteSetting(Long id);
}
