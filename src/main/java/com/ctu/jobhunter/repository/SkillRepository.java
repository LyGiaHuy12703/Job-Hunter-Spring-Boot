package com.ctu.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ctu.jobhunter.domain.Skills;

@Repository
public interface SkillRepository extends JpaRepository<Skills, String>, JpaSpecificationExecutor<Skills> {
    boolean existsByName(String name);

    List<Skills> findByIdIn(List<String> req);
}
