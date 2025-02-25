package com.tnx.posBilling.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.Company;
import com.tnx.posBilling.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company saveCompany(Company company) {
        company.setCreatedAt(LocalDateTime.now());
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company name is not found"));
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id).map(company -> {
            company.setName(updatedCompany.getName());
            company.setEmail(updatedCompany.getEmail());
            company.setPhone(updatedCompany.getPhone());
            company.setAddress(updatedCompany.getAddress());
            company.setUpdatedAt(LocalDateTime.now());
            return companyRepository.save(company);
        }).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public String deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company is not found"));
        if (company != null) {
            companyRepository.deleteById(id);
        }
        return "Company Deleted";
    }
}
