package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.RoleDTO;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    // Convert từ DTO -> Entity
    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setFullname(dto.getFullname());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setBirthday(dto.getBirthday());
        entity.setAvatar(dto.getAvatar());
        entity.setFacebookAccountId(dto.getFacebookAccountId());
        entity.setGoogleAccountId(dto.getGoogleAccountId());

        // Convert RoleDTO -> RoleEntity
        List<RoleEntity> roles = dto.getRoles().stream().map(roleDTO -> {
            RoleEntity role = new RoleEntity();
            role.setId(roleDTO.getId());  // Nếu RoleDTO có ID
            role.setName(roleDTO.getName());
            return role;
        }).collect(Collectors.toList());

        entity.setRoles(roles);
        return entity;
    }
    // Convert từ DTO -> Entity
    public UserEntity toEntity(UserLoginDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setFullname(dto.getFullname());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setBirthday(LocalDate.from(dto.getBirthday()));
        entity.setAvatar(dto.getAvatar());
        entity.setFacebookAccountId(dto.getFacebookAccountId());
        entity.setGoogleAccountId(dto.getGoogleAccountId());

        // Convert RoleDTO -> RoleEntity
        List<RoleEntity> roles = dto.getRoles().stream().map(roleDTO -> {
            RoleEntity role = new RoleEntity();
            role.setId(roleDTO.getId());  // Nếu RoleDTO có ID
            role.setName(roleDTO.getName());
            return role;
        }).collect(Collectors.toList());

        entity.setRoles(roles);
        return entity;
    }

    // Convert từ Entity -> DTO
    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(entity.getUsername());
        dto.setFullname(entity.getFullname());
        dto.setPassword(entity.getPassword());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setGender(entity.getGender());
        dto.setBirthday(entity.getBirthday());
        dto.setAvatar(entity.getAvatar());
        dto.setFacebookAccountId(entity.getFacebookAccountId());
        dto.setGoogleAccountId(entity.getGoogleAccountId());
        // Convert RoleEntity -> RoleDTO
        List<RoleDTO> roles = entity.getRoles().stream().map(roleEntity -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(roleEntity.getId()); // Nếu RoleEntity có ID
            roleDTO.setName(roleEntity.getName());
            return roleDTO;
        }).collect(Collectors.toList());

        dto.setRoles(roles);
        return dto;
    }

    // Convert từ DTO -> Entity (cập nhật dữ liệu)
    public UserEntity toEntity(UserDTO dto, UserEntity entity) {
        entity.setUsername(dto.getUsername());
        entity.setFullname(dto.getFullname());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setBirthday(dto.getBirthday());
        entity.setAvatar(dto.getAvatar());
        entity.setFacebookAccountId(dto.getFacebookAccountId());
        entity.setGoogleAccountId(dto.getGoogleAccountId());
        List<RoleEntity> roles = dto.getRoles().stream().map(roleDTO -> {
            RoleEntity role = new RoleEntity();
            role.setId(roleDTO.getId());
            role.setName(roleDTO.getName());
            return role;
        }).collect(Collectors.toList());

        entity.setRoles(roles);
        return entity;
    }
}
