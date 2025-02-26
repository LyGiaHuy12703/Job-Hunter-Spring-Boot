package com.ctu.jobhunter.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoginDTO {
    // @NotBlank(message = "email không được để trống!")
    String username;
    @NotBlank(message = "password không được để trống!")
    String password;
}
