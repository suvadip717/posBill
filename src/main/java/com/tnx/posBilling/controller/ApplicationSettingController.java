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
import com.tnx.posBilling.service.ApplicationSettingService;

@RestController
@RequestMapping("/application-setting")
public class ApplicationSettingController {
    @Autowired
    private ApplicationSettingService settingService;

    @PostMapping
    public ResponseEntity<ApplicationSetting> createSetting(@RequestBody ApplicationSetting setting) {
        return ResponseEntity.ok(settingService.saveSetting(setting));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationSetting>> getAllSettings() {
        return ResponseEntity.ok(settingService.getAllSettings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationSetting> getSettingById(@PathVariable Long id) {
        ApplicationSetting setting = settingService.getSettingById(id);
        return ResponseEntity.ok(setting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationSetting> updateSetting(@PathVariable Long id,
            @RequestBody ApplicationSetting setting) {
        return ResponseEntity.ok(settingService.updateSetting(id, setting));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        settingService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
