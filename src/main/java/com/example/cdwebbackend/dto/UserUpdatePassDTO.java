package com.example.cdwebbackend.dto;
import com.example.cdwebbackend.validation.PasswordMatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@PasswordMatch
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatePassDTO {
    @NotBlank(message = "{validation.password_notBlank}")
    private String password; // Mật khẩu cũ

    @NotBlank(message = "{validation.newPassword_notBlank}")
    @Size(min = 7, message = "{validation.newPassword_size}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "{validation.newPassword_pattern}")
    private String newPassword; // Mật khẩu mới

    @NotBlank(message = "{validation.retypePassword_notBlank}")
    @JsonProperty("reNewPassword")
    private String retypePassword; // Nhập lại mật khẩu mới

    // Getters và Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
}