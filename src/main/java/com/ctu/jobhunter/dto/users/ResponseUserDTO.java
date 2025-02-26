package com.ctu.jobhunter.dto.users;

import java.time.Instant;

import com.ctu.jobhunter.utils.constant.GenderEnum;

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
public class ResponseUserDTO {
    String id;
    String name;
    GenderEnum gender;
    String address;
    Integer age;
    Instant createdAt;
    Instant updatedAt;
}
