package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.dto.LoginRequestDTO;
import com.moneyware.bank.documentservice.dto.LoginResponseDTO;
import com.moneyware.bank.documentservice.entity.UserEntity;
import com.moneyware.bank.documentservice.exception.CustomException;
import com.moneyware.bank.documentservice.repository.UserRepository;
import com.moneyware.bank.documentservice.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ContextConfiguration(classes = {UserDetailImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailImplTest {


    public static final String TEST_USER_NAME = "TestUserName";
    public static final String TEST_PASSWORD = "TestPassword";
    public static final String JWT_TOKEN = "JWTToken";
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailImpl userDetail;

    @Test
    void saveUserTest() {
        Mockito.when(userRepository.save(any())).thenReturn(getUserEntity());
        UserEntity userEntity = userDetail.saveUser(getUserEntity());
        assertEquals(1, userEntity.getId());

    }

    @Test
    void loginUserTest() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(getUserEntity());
        Mockito.when(jwtUtil.generateToken(getUserEntity())).thenReturn(JWT_TOKEN);
        LoginResponseDTO loginResponseDTO = userDetail.loginUser(getLoginRequestDTO());
        assertEquals(JWT_TOKEN, loginResponseDTO.getJwttoken());
    }

    @Test
    void loginUserTestException() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(null);

        CustomException thrown = assertThrows(
                CustomException.class,
                () -> {
                    userDetail.loginUser(getLoginRequestDTO());
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
    }

    @Test
    void getAllUser() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(getUserEntity(), getUserEntity()));
        List<UserEntity> list = userDetail.getAllUser();
        assertEquals(2, list.size());
    }

    UserEntity getUserEntity() {
        return new UserEntity(1, TEST_USER_NAME, TEST_PASSWORD);
    }

    LoginRequestDTO getLoginRequestDTO() {
        return new LoginRequestDTO(TEST_USER_NAME, TEST_PASSWORD);
    }
}