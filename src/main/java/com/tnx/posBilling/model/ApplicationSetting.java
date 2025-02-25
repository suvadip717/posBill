package com.tnx.posBilling.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "application_settings")
public class ApplicationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String settingKey;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private SettingType settingType; // Enum for BOOLEAN, DROPDOWN, MULTISELECT

    @Column(columnDefinition = "JSON")
    private String options; // Stores dropdown/multiselect options in JSON format

    private String defaultValue;

    public enum SettingType {
        BOOLEAN, DROPDOWN, MULTISELECT
    }
}
