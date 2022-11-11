package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.dto.LoginRequestDTO;
import com.moneyware.bank.documentservice.dto.LoginResponseDTO;
import com.moneyware.bank.documentservice.entity.UserEntity;
import com.moneyware.bank.documentservice.exception.CustomException;
import com.moneyware.bank.documentservice.repository.UserRepository;
import com.moneyware.bank.documentservice.security.JwtUtil;
import com.moneyware.bank.documentservice.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.moneyware.bank.documentservice.constants.MessageConstants.INCORRECT_USER_NAME_PASSWORD_PROVIDED;
import static com.moneyware.bank.documentservice.constants.MessageConstants.USER_DOES_NOT_EXIST;

@Service
public class UserDetailImpl implements UserDetail {


    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;


    @Autowired
    public UserDetailImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        if (userEntity != null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User Already Exist with username " + user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        UserEntity userEntity = userRepository.findByUsername(loginRequestDTO.getUsername());
        if (userEntity == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, USER_DOES_NOT_EXIST);
        }
        if (userEntity.getPassword().equals(loginRequestDTO.getPassword())) {
            String token = jwtUtil.generateToken(userEntity);
            return new LoginResponseDTO(token);
        }
        throw new CustomException(HttpStatus.UNAUTHORIZED, INCORRECT_USER_NAME_PASSWORD_PROVIDED);
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }


}
