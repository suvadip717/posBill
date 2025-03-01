package com.tnx.posBilling.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @Column(unique = true, nullable = false, length = 40)
    private String settingKey;

    @Column(columnDefinition = "TEXT", length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    private SettingType settingType;

    @Column(columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class) // Use the converter
    private List<String> options;

    @Column(length = 40)
    private String defaultValue;

    public enum SettingType {
        BOOLEAN, DROPDOWN, MULTISELECT
    }
}
