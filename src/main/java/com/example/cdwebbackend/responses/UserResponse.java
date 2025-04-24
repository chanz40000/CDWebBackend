package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserResponse {
    @Column(name="id")
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name = "fullname")
    private String fullname;
    @Column
    private String password;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @ManyToMany
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private List<RoleEntity> roles = new ArrayList<>();
    @Column(name = "gender")
    String gender;
    @Column(name = "birthday")
    LocalDate birthday;
    @Column(name = "avatar")
    String avatar;

    @Column(name = "facebook_account_id")
    String facebookAccountId;

    @Column(name = "google_account_id")
    String googleAccountId;


    public static UserResponse fromUser(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .phone(user.getPhone())
                .address(user.getAddress())
                .birthday(user.getBirthday())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .roles(user.getRoles()) // Nếu role là Enum
                .build();
    }
}
