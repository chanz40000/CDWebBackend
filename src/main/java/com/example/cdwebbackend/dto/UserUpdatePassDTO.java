package com.example.cdwebbackend.dto;
import com.example.cdwebbackend.validation.PasswordMatch;
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
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String password; // Mật khẩu cũ

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 7, message = "Mật khẩu mới phải ít nhất 7 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một số")
    private String newPassword; // Mật khẩu mới

    @NotBlank(message = "Nhập lại mật khẩu không được để trống")
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
