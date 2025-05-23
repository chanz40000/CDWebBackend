//package com.example.cdwebbackend.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserDTO extends AbstractDTO<UserDTO> {
//    @JsonProperty("username")
//    private String username;
//
//    @JsonProperty("fullname")
//    private String fullname;
//
//    @JsonProperty("password")
//    private String password;
//
//    @JsonProperty("phone")
//    private String phone;
//
//    @JsonProperty("email")
//    private String email;
//
//    @JsonProperty("address")
//    private String address;
//
//    @JsonProperty("roles")
//    private List<RoleDTO> roles = new ArrayList<>();
//
//    @JsonProperty("gender")
//    private String gender;
//
//    @JsonProperty("birthday")
//    private LocalDate birthday;
//
//    @JsonProperty("avatar")
//    private String avatar;
//
//    @JsonProperty("facebook_account_id")
//    private String facebookAccountId;
//
//    @JsonProperty("google_account_id")
//    private String googleAccountId;
//
//    @JsonProperty("retype_password")
//    private String retypePassword;
//
//    public String getRetypePassword() {
//        return retypePassword;
//    }
//
//    public void setRetypePassword(String retypePassword) {
//        this.retypePassword = retypePassword;
//    }
//    // Getters and Setters
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getFullname() {
//        return fullname;
//    }
//
//    public void setFullname(String fullname) {
//        this.fullname = fullname;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public List<RoleDTO> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<RoleDTO> roles) {
//        this.roles = roles;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public LocalDate getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(LocalDate birthday) {
//        this.birthday = birthday;
//    }
//
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public String getFacebookAccountId() {
//        return facebookAccountId;
//    }
//
//    public void setFacebookAccountId(String facebookAccountId) {
//        this.facebookAccountId = facebookAccountId;
//    }
//
//    public String getGoogleAccountId() {
//        return googleAccountId;
//    }
//
//    public void setGoogleAccountId(String googleAccountId) {
//        this.googleAccountId = googleAccountId;
//    }
//}
package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.validation.PasswordMatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatch
public class UserDTO extends AbstractDTO<UserDTO> {

    @JsonProperty("username")
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    private String username;

    @JsonProperty("fullname")
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
    private String fullname;

    @JsonProperty("password")
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 7, message = "Mật khẩu phải có ít nhất 7 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một số")
    private String password;

    @JsonProperty("email")
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @JsonProperty("phone")
    @Size(max = 10, message = "Số điện thoại không được vượt quá 10 ký tự")
    private String phone;

    @JsonProperty("address")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @JsonProperty("roles")
    private List<RoleDTO> roles = new ArrayList<>();

    @JsonProperty("gender")
    @Size(max = 20, message = "Giới tính không được vượt quá 20 ký tự")
    private String gender;

    @JsonProperty("birthday")
    private LocalDate birthday;

    @JsonProperty("avatar")
    @Size(max = 255, message = "URL ảnh đại diện không được vượt quá 255 ký tự")
    private String avatar;

    @JsonProperty("facebook_account_id")
    @Size(max = 100, message = "ID tài khoản Facebook không được vượt quá 100 ký tự")
    private String facebookAccountId;

    @JsonProperty("google_account_id")
    @Size(max = 100, message = "ID tài khoản Google không được vượt quá 100 ký tự")
    private String googleAccountId;

    @JsonProperty("retype_password")
    @Size(max = 255, message = "Mật khẩu nhập lại không được vượt quá 255 ký tự")
    private String retypePassword;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFacebookAccountId() {
        return facebookAccountId;
    }

    public void setFacebookAccountId(String facebookAccountId) {
        this.facebookAccountId = facebookAccountId;
    }

    public String getGoogleAccountId() {
        return googleAccountId;
    }

    public void setGoogleAccountId(String googleAccountId) {
        this.googleAccountId = googleAccountId;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
}