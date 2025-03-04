package com.ctu.jobhunter.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.Company;
import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.api.RestResponse;
import com.ctu.jobhunter.dto.company.CompanyRequest;
import com.ctu.jobhunter.dto.company.CompanyResponse;
import com.ctu.jobhunter.dto.pagination.Meta;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.repository.CompanyRepository;
import com.ctu.jobhunter.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .logo(request.getLogo())
                .description(request.getDescription())
                .build();
        companyRepository.save(company);
        return company;
    }

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = companyRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageCompanies.getTotalPages())
                .total(pageCompanies.getTotalElements())
                .build();
        return ResultPaginationDTO.builder()
                .meta(meta)
                .result(pageCompanies.getContent())
                .build();
    }

    public Company updateCompany(String id, CompanyRequest request) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new UnsupportedOperationException("Unimplemented method 'deleteCompany'");
        }
        company.setAddress(request.getAddress());
        company.setDescription(request.getDescription());
        company.setName(request.getName());
        company.setLogo(request.getLogo());
        companyRepository.save(company);
        return company;
    }

    public void deleteCompany(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new IdInvalidException("Company not exist");
        }
        List<User> users = userRepository.findByCompany(company);
        this.userRepository.deleteAll(users);
        companyRepository.delete(company);
    }

    public Company findById(String idCompany) {
        return companyRepository.findById(idCompany).orElse(null);
    }

}
