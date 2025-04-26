package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.converter.UserConverter;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.dto.UserLoginDTO;
import com.example.cdwebbackend.entity.RoleEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.exceptions.PermissionDenyException;
import com.example.cdwebbackend.repository.RoleRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public UserDTO findOneById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findOneById(id);
        return userConverter.toDTO(userEntity.get());
    }

    @Override
    @Transactional
    public UserEntity createUser(UserDTO userDTO) throws Exception {
        String username = userDTO.getUsername();
        //kiem tra xem username da co  hay chua
        if(userRepository.findOneByUsername(username).isPresent()) {
            throw new DataIntegrityViolationException("Username đã tồn tại!");
        }

        for (int i=0; i<userDTO.getRoles().size(); i++){
            RoleEntity role = roleRepository.findOneById(userDTO.getRoles().get(i).getId())
                    .orElseThrow(() -> {
                        return new DataNotFoundException(  "Role not found");
                    });

            if (role.getName().toUpperCase().equals(RoleEntity.ADMIN)){
                throw new PermissionDenyException("You cannot register a admin account");
            }
        }


        UserEntity newUser = userConverter.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        RoleEntity roleUser = roleRepository.findOneByName("USER")
                .orElseThrow(() -> new DataNotFoundException("Default user role not found"));
        newUser.setRoles(List.of(roleUser));
        newUser = userRepository.save(newUser);
        return newUser;
    }

    @Override
    @Transactional
    public String login(UserLoginDTO userLoginDTO) throws Exception {
//
        Optional<UserEntity> userOpt = Optional.empty();

        String googleAccountId = userLoginDTO.getGoogleAccountId();
        String facebookAccountId = userLoginDTO.getFacebookAccountId();

        if (googleAccountId != null) {
            userOpt = userRepository.findOneByGoogleAccountId(googleAccountId);
        } else if (facebookAccountId != null) {
            userOpt = userRepository.findOneByFacebookAccountId(facebookAccountId);
        } else {
            userOpt = userRepository.findOneByUsername(userLoginDTO.getUsername());
        }


        // Kiểm tra trường hợp nếu người dùng đăng nhập bằng Google hoặc Facebook
        if (userOpt.isEmpty() && (googleAccountId != null || facebookAccountId != null)) {
            System.out.println("nguoi dung dang dang nhap bang google hoac facebook");
            // Trường hợp Google hoặc Facebook, tạo mới người dùng nếu chưa tồn tại
            if (googleAccountId != null) {
                userOpt = userRepository.findOneByGoogleAccountId(googleAccountId);
                System.out.println("dang nhap bang google");
            } else if (facebookAccountId != null) {
                userOpt = userRepository.findOneByFacebookAccountId(facebookAccountId);
            }

            // Nếu người dùng chưa tồn tại, tạo mới người dùng
//            if (userOpt.isEmpty()) {
//                System.out.println("Tao moi nguoi dung");
//                UserEntity userEntity = new UserEntity();
//                userEntity.setGoogleAccountId(googleAccountId);
//                userEntity.setFacebookAccountId(facebookAccountId);
//                userEntity.setEmail(userLoginDTO.getEmail());  // Lấy email từ DTO
//                // Cập nhật các trường cần thiết khác từ userLoginDTO
//                userEntity = userRepository.save(userEntity);
//
//                userOpt = Optional.of(userEntity);
//            }
            // Nếu người dùng chưa tồn tại, tạo mới người dùng
            if (userOpt.isEmpty()) {
                System.out.println("Tao moi nguoi dung");
                UserEntity userEntity = new UserEntity();
                userEntity.setGoogleAccountId(googleAccountId);
                userEntity.setFacebookAccountId(facebookAccountId);
                userEntity.setEmail(userLoginDTO.getEmail());  // Lấy email từ DTO

                // ✅ THÊM CÁC DÒNG NÀY:
                userEntity.setUsername(userLoginDTO.getEmail()); // dùng email làm username
                RoleEntity roleUser = roleRepository.findOneByName("USER")
                        .orElseThrow(() -> new DataNotFoundException("Default user role not found"));
                userEntity.setRoles(List.of(roleUser));

                // Cập nhật các trường cần thiết khác từ userLoginDTO (nếu có)
                userEntity = userRepository.save(userEntity);
                userOpt = Optional.of(userEntity);
            }

            // Tạo và trả về JWT token cho người dùng
            System.out.println("token tra ve: "+ jwtTokenUtil.generateToken(userOpt.get()));
            return jwtTokenUtil.generateToken(userOpt.get());
        }

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid username / password");
        }

        UserEntity userEntity = userOpt.get();

        // Kiểm tra trường hợp đăng nhập qua Google hoặc Facebook
        if (userEntity.getGoogleAccountId() != null || userEntity.getFacebookAccountId() != null) {
            // Nếu có Google hoặc Facebook Account ID, không cần kiểm tra mật khẩu
            return jwtTokenUtil.generateToken(userEntity);
        }
        // Kiểm tra mật khẩu chính xác
        if (userEntity.getFacebookAccountId() == null && userEntity.getGoogleAccountId() == null) {
            if (!passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword())) {
                throw new BadCredentialsException("password not correct");
            }
        }

        // Thực hiện xác thực thông qua UsernamePasswordAuthenticationToken nếu mật khẩu đúng
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userEntity.getPassword()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
        return jwtTokenUtil.generateToken(userEntity);
    }
    @Override
    public UserDTO findOneByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findOneByUsername(username);
        return userEntity.map(userConverter::toDTO).orElse(null);
    }

    @Override
    public boolean checkPassword(String oldPassword, String password) {
        System.out.println("old: "+oldPassword);
        System.out.println("pw: "+ password);
        String oldPassEncoder = passwordEncoder.encode(oldPassword);
        System.out.println("oldEn: "+oldPassEncoder);
        if(passwordEncoder.matches(oldPassword, password)){
            System.out.println("trung roi nha");
            return true;
        }
        System.out.println("chua trung");
        return false;
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

    @Override
    public UserEntity getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String username = jwtTokenUtil.extractUsername(token);
        Optional<UserEntity>userEntity = userRepository.findOneByUsername(username);

        if(userEntity.isPresent()){
            return userEntity.get();
        }else {
            throw new Exception("User not found");
        }
    }

    @Override
    public UserEntity updateUser(UserDTO userDTO, Long userId) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public UserEntity updatePassword(String newPassword, long userId) throws DataNotFoundException {
        UserEntity existingUser = userRepository.findOneById(userId)
                .orElseThrow(()-> new DataNotFoundException("User not found"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        System.out.println("Da doi pass word");
        return existingUser;
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserDTO userDTO, long userId) throws DataNotFoundException {
        UserEntity existingUser = userRepository.findOneById(userId)
        .orElseThrow(()-> new DataNotFoundException("User not found"));

        //Check if the user is being changed and if it already exists for another user
        String username = userDTO.getUsername();
        if(!existingUser.getUsername().equals(username)&&
        userRepository.existsByUsername(username)){
            throw new DataIntegrityViolationException("Username already exists");
        }
        RoleEntity updateRole = roleRepository.findOneById(existingUser.getRoles().get(0).getId())
                .orElseThrow(() -> new DataNotFoundException("Role does not exist"));


        //check ì the role, admin can do this
        if(updateRole.getName().equalsIgnoreCase(RoleEntity.ADMIN)){
            try {
                throw new PermissionDenyException("You can not update to an admin account");
            } catch (PermissionDenyException e) {
                throw new RuntimeException(e);
            }
        }
        if (userDTO.getFullname() != null) {
            existingUser.setFullname(userDTO.getFullname());
        }

        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }

        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }

        if (userDTO.getBirthday() != null) {
            existingUser.setBirthday(userDTO.getBirthday());
        }

        if (userDTO.getGoogleAccountId() != null) {
            existingUser.setGoogleAccountId(userDTO.getGoogleAccountId());
        }

        if (userDTO.getFacebookAccountId() != null) {
            existingUser.setFacebookAccountId(userDTO.getFacebookAccountId());
        }

        return existingUser;
    }

    @Override
    @Transactional
    public UserEntity updateAvatar(long userId, String imageUrl) throws DataNotFoundException {
        UserEntity existingUser = userRepository.findOneById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Avatar URL cannot be null or empty");
        }

        existingUser.setAvatar(imageUrl);

        return userRepository.save(existingUser);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return null;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };
        String encodedPassword = passwordEncoder.encode("123");
        System.out.println(encodedPassword);
    }
}
