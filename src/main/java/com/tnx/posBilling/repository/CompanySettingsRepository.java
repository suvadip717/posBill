package com.tnx.posBilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.CompanySettings;

@Repository
public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {

}
