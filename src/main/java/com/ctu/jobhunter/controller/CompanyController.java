package com.ctu.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.domain.Company;
import com.ctu.jobhunter.dto.api.RestResponse;
import com.ctu.jobhunter.dto.company.CompanyRequest;
import com.ctu.jobhunter.dto.company.CompanyResponse;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequest request) {
        Company company = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> fetchAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        ResultPaginationDTO companies = companyService.fetchAllCompanies(spec, pageable);
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") String id,
            @Valid @RequestBody CompanyRequest request) {
        Company company = companyService.updateCompany(id, request);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable("id") String id) {
        companyService.deleteCompany(id);
    }

}