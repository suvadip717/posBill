package com.tnx.posBilling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
