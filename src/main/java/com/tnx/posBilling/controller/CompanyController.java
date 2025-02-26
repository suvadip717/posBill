package com.tnx.posBilling.controller;

import java.util.List;

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

import com.tnx.posBilling.model.Company;
import com.tnx.posBilling.service.impl.CompanyServiceImpl;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyServiceImpl companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return companyService.saveCompany(company);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
        // return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        return companyService.updateCompany(id, updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        return companyService.deleteCompany(id);
    }
}
