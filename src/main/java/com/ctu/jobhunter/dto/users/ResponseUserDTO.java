package com.ctu.jobhunter.dto.users;

import java.time.Instant;

import com.ctu.jobhunter.domain.Company;
import com.ctu.jobhunter.utils.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserDTO {
    String id;
    String email;
    String name;
    GenderEnum gender;
    String address;
    Integer age;
    Instant createdAt;
    Instant updatedAt;
    Company company;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Company {
        String id;
        String name;
    }
}
