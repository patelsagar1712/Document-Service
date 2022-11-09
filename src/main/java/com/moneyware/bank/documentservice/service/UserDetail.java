package com.moneyware.bank.documentservice.service;

import com.moneyware.bank.documentservice.dto.LoginRequestDTO;
import com.moneyware.bank.documentservice.dto.LoginResponseDTO;
import com.moneyware.bank.documentservice.entity.UserEntity;

import java.util.List;

public interface UserDetail {
    UserEntity saveUser(UserEntity user);

    LoginResponseDTO loginUser(LoginRequestDTO user);

    List<UserEntity> getAllUser();
}
