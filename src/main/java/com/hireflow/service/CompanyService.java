package com.hireflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hireflow.entity.Company;
import com.hireflow.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> searchCompanies(String companyName) {
        return companyRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}