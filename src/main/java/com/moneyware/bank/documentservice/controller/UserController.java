package com.moneyware.bank.documentservice.controller;

import com.moneyware.bank.documentservice.dto.ResponseDTO;
import com.moneyware.bank.documentservice.entity.UserEntity;
import com.moneyware.bank.documentservice.service.UserDetail;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.moneyware.bank.documentservice.constants.MessageConstants.CREATE_USER_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/user")
@CrossOrigin()
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    @Autowired
    UserDetail userDetail;

    @PostMapping("/createUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userDetail.saveUser(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(CREATE_USER_SUCCESS_MESSAGE + userEntity1.getId()));
    }
}
