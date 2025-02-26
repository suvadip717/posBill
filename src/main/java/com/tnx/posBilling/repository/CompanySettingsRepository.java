package com.tnx.posBilling.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.CompanySettings;

@Repository
public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {
    // @Query("SELECT cs FROM CompanySettings cs " +
    // "JOIN FETCH cs.company c " +
    // "JOIN FETCH cs.setting s " +
    // "WHERE cs.company.id = :companyId")
    // @Query("""
    // SELECT cs
    // FROM CompanySettings cs
    // RIGHT JOIN ApplicationSetting s ON cs.setting.id = s.id
    // WHERE cs.company.id = :companyId OR cs.company.id IS NULL
    // """)
    // List<CompanySettings> findAllSettingsByCompanyId(@Param("companyId") Long
    // companyId);
    @Query("""
                SELECT cs
                FROM CompanySettings cs
                RIGHT JOIN ApplicationSetting s ON cs.setting.id = s.id
                WHERE cs.company.id = :companyId OR cs.company.id IS NULL
            """)
    List<CompanySettings> findAllSettingsByCompanyId(@Param("companyId") Long companyId);

    List<CompanySettings> findSettingsByCompanyId(@Param("companyId") Long companyId);

    Optional<CompanySettings> findByCompanyIdAndSettingId(Long companyId, Long settingId);

    List<CompanySettings> findByCompanyId(Long companyId);
}
