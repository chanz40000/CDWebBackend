package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@NonNull
@AllArgsConstructor
@Getter
@Setter
public class UserLoginDTO {

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

}