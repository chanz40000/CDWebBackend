package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO<UserDTO> {
    @JsonProperty("username")
    private String username;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("roles")
    private List<RoleDTO> roles = new ArrayList<>();

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthday")
    private LocalDateTime birthday;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("facebook_account_id")
    private String facebookAccountId;

    @JsonProperty("google_account_id")
    private String googleAccountId;

    @JsonProperty("retype_password")
    private String retypePassword;

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
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
}
