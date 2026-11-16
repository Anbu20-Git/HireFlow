package com.hireflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Company;
import com.hireflow.service.CompanyService;

@Controller
@RequestMapping("/admin/companies")
public class AdminCompanyController {

    private final CompanyService companyService;

    public AdminCompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String manageCompanies(Model model) {

        List<Company> companies = companyService.getAllCompanies();

        model.addAttribute("companies", companies);
        model.addAttribute("totalCompanies", companies.size());

        return "admin/manage-companies";
    }
}