package com.tnx.posBilling.controller;

import java.util.*;

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

import com.tnx.posBilling.model.ApplicationSetting;
import com.tnx.posBilling.service.impl.ApplicationSettingServiceImpl;

@RestController
@RequestMapping("/api/settings")
public class ApplicationSettingController {
    @Autowired
    private ApplicationSettingServiceImpl settingService;

    @PostMapping
    public ResponseEntity<ApplicationSetting> createSetting(@RequestBody ApplicationSetting setting) {
        return settingService.saveSetting(setting);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationSetting>> getAllSettings() {
        return settingService.getAllSettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationSetting> getSettingById(@PathVariable Long id) {
        return settingService.getSettingById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationSetting> updateSetting(@PathVariable Long id,
            @RequestBody ApplicationSetting setting) {
        return settingService.updateSetting(id, setting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSetting(@PathVariable Long id) {
        return settingService.deleteSetting(id);
    }
}
