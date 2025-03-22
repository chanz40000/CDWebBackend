package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAll(Pageable pageable);
     UserDTO findOneById(Long id);
    UserDTO findOneByUsername(String username);
    public List<UserDTO> getAllUsers() ;

    public Optional<UserDTO> getUserById(Long id);

    public UserDTO saveUser(UserDTO user) ;

    public void deleteUser(Long id) ;

    public Optional<UserDTO> findByEmail(String email);
}
