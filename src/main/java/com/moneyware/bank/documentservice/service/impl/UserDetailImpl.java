package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.entity.UserEntity;
import com.moneyware.bank.documentservice.repository.UserRepository;
import com.moneyware.bank.documentservice.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailImpl implements UserDetail {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
