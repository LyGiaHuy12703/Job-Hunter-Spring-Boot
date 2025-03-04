package com.ctu.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.domain.Skills;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.dto.skill.RequestSkill;
import com.ctu.jobhunter.service.SkillService;
import com.ctu.jobhunter.utils.anotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create skill successful")
    public ResponseEntity<Skills> createSkill(@Valid @RequestBody RequestSkill request) {
        System.out.println("Received request: " + request);
        System.out.println("Skill name: " + request.getName());
        Skills skills = skillService.createSkill(request);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> fetchAllSkills(@Filter Specification<Skills> spec, Pageable pageable) {
        ResultPaginationDTO skills = skillService.fetchAllSkills(spec, pageable);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Fetch skills")
    public ResponseEntity<Skills> fetchSkill(@PathVariable("id") String id) {
        Skills skill = skillService.fetchSkill(id);
        return ResponseEntity.ok(skill);
    }

    @PutMapping("skills/{id}")
    @ApiMessage("Update skill successful")
    public ResponseEntity<Skills> updateSkill(@PathVariable("id") String id, @Valid @RequestBody Skills request) {
        Skills skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete skill successful")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") String id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(null);
    }

}
