package com.example.cdwebbackend.validation;

//import com.example.cdwebbackend.dto.UserDTO;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDTO> {
//    @Override
//    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
//        return userDTO.getPassword() != null && userDTO.getPassword().equals(userDTO.getRetypePassword());
//    }
//}
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.dto.UserUpdatePassDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof UserDTO) {
            UserDTO userDTO = (UserDTO) obj;
            return userDTO.getPassword() != null && userDTO.getPassword().equals(userDTO.getRetypePassword());
        } else if (obj instanceof UserUpdatePassDTO) {
            UserUpdatePassDTO userDTO = (UserUpdatePassDTO) obj;
            return userDTO.getNewPassword() != null && userDTO.getNewPassword().equals(userDTO.getRetypePassword());
        }
        return true; // Hoặc ném ngoại lệ nếu không hỗ trợ
    }
}
