package com.ctu.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.domain.Jobs;
import com.ctu.jobhunter.dto.jobs.ResponseJobsDTO;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.service.JobService;
import com.ctu.jobhunter.utils.anotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create job successful")
    public ResponseEntity<ResponseJobsDTO> createJobs(@RequestBody @Valid Jobs j) {
        return ResponseEntity.ok(jobService.createJobs(j));
    }

    @GetMapping("/jobs")
    @ApiMessage("Fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> fetchAllJobs(@Filter Specification<Jobs> spec, Pageable pageable) {
        ResultPaginationDTO dto = jobService.fetchAllJobs(spec, pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch job")
    public ResponseEntity<ResponseJobsDTO> fetchJobs(@PathVariable("id") String id) {
        return ResponseEntity.ok(jobService.fetchJob(id));
    }

    @PutMapping("/jobs/{id}")
    @ApiMessage("Update jobs")
    public ResponseEntity<ResponseJobsDTO> updateJobs(@RequestBody @Valid Jobs job, @PathVariable("id") String id) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete jobs")
    public ResponseEntity<Void> deleteJobs(@PathVariable("id") String id) {
        jobService.deleteJobs(id);
        return ResponseEntity.ok(null);
    }
}
