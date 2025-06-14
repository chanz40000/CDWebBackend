package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @JsonProperty("username")
    @NotBlank(message = "validation.username_notBlank")
    @Size(min = 3, max = 50, message = "validation.username_size")
    private String username;

    @JsonProperty("fullname")
    @NotBlank(message = "validation.fullname_notBlank")
    @Size(min = 2, max = 100, message = "validation.fullname_size")
    private String fullname;

    @JsonProperty("email")
    @NotBlank(message = "validation.email_notBlank")
    @Email(message = "validation.email_invalid")
    @Size(max = 100, message = "validation.email_size")
    private String email;

    @JsonProperty("phone")
    @Size(max = 10, message = "validation.phone_size")
    private String phone;

    @JsonProperty("address")
    @Size(max = 255, message = "validation.address_size")
    private String address;

    @JsonProperty("gender")
    @Size(max = 20, message = "validation.gender_size")
    private String gender;

    @JsonProperty("birthday")
    private LocalDate birthday;

    @JsonProperty("avatar")
    @Size(max = 255, message = "validation.avatar_size")
    private String avatar;

    @JsonProperty("facebook_account_id")
    @Size(max = 100, message = "validation.facebookAccountId_size")
    private String facebookAccountId;

    @JsonProperty("google_account_id")
    @Size(max = 100, message = "validation.googleAccountId_size")
    private String googleAccountId;

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
}