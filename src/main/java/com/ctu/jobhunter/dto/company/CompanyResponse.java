package com.ctu.jobhunter.dto.company;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    String id;
    String name;
    String description;
    String address;
    String logo;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
}
