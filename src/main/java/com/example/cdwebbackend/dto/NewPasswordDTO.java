package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDTO {

    @JsonProperty("email")
    @NotBlank(message = "{validation.email_notBlank}")
    @Email(message = "{validation.email_invalid}")
    private String email;

    @JsonProperty("newPassword")
    @NotBlank(message = "{validation.newPassword_notBlank}")
    @Size(min = 7, message = "{validation.newPassword_size}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "{validation.newPassword_pattern}")
    private String newPassword;
}