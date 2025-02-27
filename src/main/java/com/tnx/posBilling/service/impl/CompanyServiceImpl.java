package com.tnx.posBilling.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Company;
import com.tnx.posBilling.repository.CompanyRepository;
import com.tnx.posBilling.service.interfaces.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public ResponseEntity<Company> saveCompany(Company company) {
        company.setCreatedAt(LocalDateTime.now());
        return new ResponseEntity<>(companyRepository.save(company), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @Override
    public ResponseEntity<Company> getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company name is not found"));
        return ResponseEntity.ok(company);
    }

    @Override
    public ResponseEntity<Company> updateCompany(Long id, Company updatedCompany) {
        Company existCompany = companyRepository.findById(id).map(company -> {
            company.setName(updatedCompany.getName());
            company.setEmail(updatedCompany.getEmail());
            company.setPhone(updatedCompany.getPhone());
            company.setAddress(updatedCompany.getAddress());
            company.setUpdatedAt(LocalDateTime.now());
            return companyRepository.save(company);
        }).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return ResponseEntity.ok(existCompany);
    }

    @Override
    public ResponseEntity<String> deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company is not found"));
        if (company != null) {
            companyRepository.deleteById(id);
        }
        return ResponseEntity.ok("Company Deleted");
    }
}
