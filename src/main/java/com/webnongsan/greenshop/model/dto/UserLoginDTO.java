package com.webnongsan.greenshop.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
