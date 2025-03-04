package com.ctu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ctu.jobhunter.domain.Jobs;

@Repository
public interface JobRepository extends JpaRepository<Jobs, String>, JpaSpecificationExecutor<Jobs> {
}
