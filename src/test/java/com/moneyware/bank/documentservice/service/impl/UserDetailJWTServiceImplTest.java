package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.exception.CustomException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_SECRET;
import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ContextConfiguration(classes = {UserDetailJWTServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class UserDetailJWTServiceImplTest {

    public static final String INVALID_USER_NAME = "Invalid UserName";

    @Test
    public void testLoadUserByUsername() {
        UserDetailsService userDetailsService = new UserDetailJWTServiceImpl();
        UserDetails userDetails = userDetailsService.loadUserByUsername(JWT_USER);

        Assert.assertEquals(userDetails.getPassword(), JWT_SECRET);
    }

    @Test
    public void testLoadUserByInvalidUsername() {
        UserDetailsService userDetailsService = new UserDetailJWTServiceImpl();
        CustomException thrown = assertThrows(
                CustomException.class,
                () -> {
                    userDetailsService.loadUserByUsername(INVALID_USER_NAME);
                }
        );
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getHttpStatus());
    }
}