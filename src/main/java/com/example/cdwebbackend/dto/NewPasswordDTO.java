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
    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;

    @JsonProperty("newPassword")
    @NotBlank(message = "Mật khẩu mới là bắt buộc")
    @Size(min = 7, message = "Mật khẩu phải có ít nhất 7 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một số")
    private String newPassword;
}