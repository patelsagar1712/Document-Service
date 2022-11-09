package com.moneyware.bank.documentservice.controller;

import com.moneyware.bank.documentservice.dto.LoginRequestDTO;
import com.moneyware.bank.documentservice.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

class LoginControllerTest {
    private JwtUtil mockJwtUtil = Mockito.mock(JwtUtil.class);

    private UserDetailsService mockUserDetails = Mockito.mock(UserDetailsService.class);

    private AuthenticationManager mockAuthenticationManager = Mockito.mock(AuthenticationManager.class);

    private LoginController mockLoginController = new LoginController(mockJwtUtil, mockUserDetails, mockAuthenticationManager);

    @Test
    void createAuthenticationTokenTest() {

        try {

            mockLoginController.createAuthenticationToken(getLoginRequestDTO());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LoginRequestDTO getLoginRequestDTO() {
        return new LoginRequestDTO("TestUsername", "TestPassword");

    }

}