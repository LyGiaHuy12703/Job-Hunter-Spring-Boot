package com.ctu.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.Skills;
import com.ctu.jobhunter.dto.pagination.Meta;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.dto.skill.RequestSkill;
import com.ctu.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public void deleteSkill(String id) {
        Skills skills = skillRepository.findById(id).orElse(null);
        if (skills == null) {
            throw new IdInvalidException("Skill không hợp lệ");
        }
        skillRepository.delete(skills);
    }

    public Skills updateSkill(String id, Skills request) {
        Skills skills = skillRepository.findById(id).orElse(null);
        if (skills == null) {
            throw new IdInvalidException("Skill không hợp lệ");
        }
        skills.setName(request.getName());
        skillRepository.save(skills);
        return skills;
    }

    public Skills fetchSkill(String id) {
        Skills skills = skillRepository.findById(id).orElse(null);
        if (skills == null) {
            throw new IdInvalidException("Skill không hợp lệ");
        }
        return skills;
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skills> spec, Pageable pageable) {
        Page<Skills> pageSkill = skillRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageSkill.getTotalPages())
                .total(pageSkill.getTotalElements())
                .build();

        return ResultPaginationDTO.builder().meta(meta).result(pageSkill.getContent()).build();
    }

    public Skills createSkill(RequestSkill request) {
        boolean checkExist = skillRepository.existsByName(request.getName());
        if (checkExist) {
            throw new IdInvalidException("Skill đã tồn tại");
        }
        Skills skills = Skills.builder().name(request.getName()).build();
        skillRepository.save(skills);
        return skills;
    }
}
