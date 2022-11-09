package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_SECRET;
import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_USER;
import static com.moneyware.bank.documentservice.constants.MessageConstants.USER_NOT_FOUND_WITH_USERNAME;

@Service
public class UserDetailJWTServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws CustomException {
        if (JWT_USER.equals(username)) {
            return new User(JWT_USER, JWT_SECRET,
                    new ArrayList<>());
        } else {
            throw new CustomException(HttpStatus.UNAUTHORIZED, USER_NOT_FOUND_WITH_USERNAME + username);
        }
    }
}
