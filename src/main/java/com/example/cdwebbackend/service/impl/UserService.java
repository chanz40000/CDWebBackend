package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;
    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
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
    public UserEntity createUser(UserDTO userDTO) throws ConfigDataNotFoundException {
        String username = userDTO.getUsername();
        //kiem tra xem username da co  hay chua
        if(userRepository.findOneByUsername(username)!=null){
            throw new DataIntegrityViolationException("Username already exist!");
        }
        UserEntity newUser = userConverter.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        newUser = userRepository.save(newUser);
        return newUser;
    }

    @Override
    public String login(String username, String password) throws Exception{
        Optional<UserEntity> user= userRepository.findOneByUsername(username);
        if(user.isEmpty()) {
            throw new DataIntegrityViolationException("Invalid username / password");
        }
        UserEntity userEntity = user.get();

        //check password
        //neu khong dang nhap = gg, facebook
        if(userEntity.getFacebookAccountId()==null&&userEntity.getGoogleAccountId()==null){
            String encodedPassword = passwordEncoder.encode(password);
            if(!encodedPassword.equals(userEntity.getPassword())){
                throw new BadCredentialsException("wrong phone number or password");
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        //authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(userEntity);//muon tra JWT token
    }


    @Override
    public UserDTO findOneByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findOneByUsername(username);
        return userEntity.map(userConverter::toDTO).orElse(null);
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
