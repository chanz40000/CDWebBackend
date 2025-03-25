package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@RequiredArgsConstructor
@NonNull
@AllArgsConstructor
@Getter
@Setter
public class UserLoginDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}