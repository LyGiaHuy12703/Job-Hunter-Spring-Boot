package com.ctu.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.Jobs;
import com.ctu.jobhunter.domain.Skills;
import com.ctu.jobhunter.dto.jobs.ResponseJobsDTO;
import com.ctu.jobhunter.dto.pagination.Meta;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.repository.CompanyRepository;
import com.ctu.jobhunter.repository.JobRepository;
import com.ctu.jobhunter.repository.SkillRepository;

import jakarta.validation.Valid;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResponseJobsDTO createJobs(Jobs j) {
        // check skill
        if (j.getSkills() != null) {
            // lấy danh sách id skill
            List<String> skills = j.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            // Tìm danh sách skill trong db
            List<Skills> dbSkills = skillRepository.findByIdIn(skills);
            j.setSkills(dbSkills);
        }
        // create job
        Jobs currentJob = jobRepository.save(j);
        // convert dto
        return convertJobsDTO(currentJob);
    }

    private ResponseJobsDTO convertJobsDTO(Jobs jobs) {
        ResponseJobsDTO dto = new ResponseJobsDTO();
        if (jobs.getSkills() != null) {
            List<String> skills = jobs.getSkills()
                    .stream().map(s -> s.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return ResponseJobsDTO.builder()
                .id(jobs.getId())
                .name(jobs.getName())
                .salary(jobs.getSalary())
                .active(jobs.isActive())
                .description(jobs.getDescription())
                .quantity(jobs.getQuantity())
                .location(jobs.getLocation())
                .level(jobs.getLevel())
                .startDate(jobs.getStartDate())
                .endDate(jobs.getEndDate())
                .createdAt(jobs.getCreatedAt())
                .createdBy(jobs.getCreatedBy())
                .updatedAt(jobs.getUpdatedAt())
                .updatedBy(jobs.getUpdatedBy())
                .build();
    }

    public ResultPaginationDTO fetchAllJobs(Specification<Jobs> spec, Pageable pageable) {
        Page<Jobs> listJob = jobRepository.findAll(spec, pageable);

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(listJob.getTotalPages())
                .total(listJob.getTotalElements())
                .build();

        return ResultPaginationDTO.builder()
                .result(listJob)
                .meta(meta)
                .build();

    }

    public ResponseJobsDTO fetchJob(String id) {
        Jobs job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            throw new IdInvalidException("Không tìm thấy job");
        }
        return convertJobsDTO(job);
    }

    public ResponseJobsDTO updateJob(String id, Jobs req) {
        Jobs job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            throw new IdInvalidException("Không tìm thấy job");
        }
        if (companyRepository.findById(req.getCompany().getId()) != null) {
            job.setCompany(req.getCompany());
        }
        job.setName(req.getName());
        job.setSalary(req.getSalary());
        job.setQuantity(req.getQuantity());
        job.setActive(req.isActive());
        job.setLocation(req.getLocation());
        job.setLevel(req.getLevel());
        job.setDescription(req.getDescription());
        job.setStartDate(req.getStartDate());
        job.setEndDate(req.getEndDate());
        jobRepository.save(job);

        return convertJobsDTO(job);
    }

    public void deleteJobs(String id) {
        Jobs job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            throw new IdInvalidException("Không tìm thấy job");
        }
        jobRepository.delete(job);
    }
}
