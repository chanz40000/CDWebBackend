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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserConverter userConverter;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
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
    @Transactional
    public UserEntity createUser(UserDTO userDTO) throws ConfigDataNotFoundException {
        String username = userDTO.getUsername();
        //kiem tra xem username da co  hay chua
        if(userRepository.findOneByUsername(username).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists!");
        }

        UserEntity newUser = userConverter.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        newUser = userRepository.save(newUser);
        return newUser;
    }

//    @Override
//    @Transactional
//    public String login(String username, String password) throws Exception {
//
//        System.out.println("DEBUG: Searching for user -> " + username);
//        Optional<UserEntity> userOpt = userRepository.findOneByUsername(username);
//        System.out.println("DEBUG: User found -> " + userOpt);
//
//        if (userOpt.isEmpty()) {
//            throw new BadCredentialsException("Invalid username / password");
//        }
//
//        UserEntity userEntity = userOpt.get();
//
//        // Kiểm tra mật khẩu chính xác
//        if (userEntity.getFacebookAccountId() == null && userEntity.getGoogleAccountId() == null) {
//            if (!passwordEncoder.matches(password, userEntity.getPassword())) {
//                throw new BadCredentialsException("password not correct");
//            }
//        }
//
//        // Xác thực với Spring Security
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//        // Trả về JWT token
//        return jwtTokenUtil.generateToken(userEntity);
//    }

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String login(String username, String password) throws Exception {

        System.out.println("DEBUG: Searching for user -> " + username);
        Optional<UserEntity> userOpt = userRepository.findOneByUsername(username);
        System.out.println("DEBUG: User found -> " + userOpt);

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid username / password");
        }

        UserEntity userEntity = userOpt.get();

        // Kiểm tra mật khẩu chính xác
        if (userEntity.getFacebookAccountId() == null && userEntity.getGoogleAccountId() == null) {
            if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                throw new BadCredentialsException("password not correct");
            }
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, userEntity.getAuthorities()));

        return jwtTokenUtil.generateToken(userEntity);
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
