package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAll(Pageable pageable);
     UserDTO findOneById(Long id);
     public UserEntity createUser(UserDTO userDTO) throws Exception;
     public String login(String username, String password) throws Exception;
    UserDTO findOneByUsername(String username);
    public List<UserDTO> getAllUsers() ;

    public Optional<UserDTO> getUserById(Long id);

    public UserDTO saveUser(UserDTO user) ;

    public void deleteUser(Long id) ;

    public Optional<UserDTO> findByEmail(String email);
}
