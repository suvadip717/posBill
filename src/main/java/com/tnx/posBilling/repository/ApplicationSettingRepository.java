package com.tnx.posBilling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.ApplicationSetting;

@Repository
public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {
    Optional<ApplicationSetting> findBySettingKey(String settingKey);
}
