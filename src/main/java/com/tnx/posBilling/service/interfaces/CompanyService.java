package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.Company;

public interface CompanyService {
    public ResponseEntity<Company> saveCompany(Company company);

    public ResponseEntity<List<Company>> getAllCompanies();

    public ResponseEntity<Company> getCompanyById(Long id);

    public ResponseEntity<Company> updateCompany(Long id, Company updatedCompany);

    public ResponseEntity<String> deleteCompany(Long id);
}
