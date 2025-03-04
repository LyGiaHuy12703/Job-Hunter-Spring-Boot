package com.ctu.jobhunter.dto.jobs;

import java.time.Instant;
import java.util.List;

import com.ctu.jobhunter.domain.Company;
import com.ctu.jobhunter.domain.Skills;
import com.ctu.jobhunter.utils.constant.LevelEnum;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ResponseJobsDTO {
    String id;
    String name;
    String location;
    double salary;
    int quantity;
    LevelEnum level;
    String description;
    Instant startDate;
    Instant endDate;
    boolean active;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    Company company;
    List<String> skills;
}
