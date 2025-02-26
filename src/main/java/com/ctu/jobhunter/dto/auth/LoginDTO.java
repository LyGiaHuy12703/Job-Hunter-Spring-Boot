package com.ctu.jobhunter.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "email không được để trống!")
    String email;
    @NotBlank(message = "password không được để trống!")
    String password;
}
