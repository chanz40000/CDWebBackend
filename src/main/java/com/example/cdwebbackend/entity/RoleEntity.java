package com.example.cdwebbackend.entity;

import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {

    @Column
    private String code;

    @Column
    private String name;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<UserEntity> users = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

}
