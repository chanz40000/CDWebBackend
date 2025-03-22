package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;
    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public UserDTO findOneById(Long id) {
        UserEntity userEntity = userRepository.findOneById(id);
        return userConverter.toDTO(userEntity);
    }

    @Override
    public UserDTO findOneByUsername(String username) {
        UserEntity userEntity = userRepository.findOneByUsername(username);
        return userConverter.toDTO(userEntity);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return null;
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return Optional.empty();
    }
}
