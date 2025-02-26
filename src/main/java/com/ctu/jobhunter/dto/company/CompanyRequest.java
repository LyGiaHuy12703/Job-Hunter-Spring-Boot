package com.ctu.jobhunter.dto.company;

import jakarta.persistence.Column;
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
public class CompanyRequest {
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;
    String address;
    String logo;
}
