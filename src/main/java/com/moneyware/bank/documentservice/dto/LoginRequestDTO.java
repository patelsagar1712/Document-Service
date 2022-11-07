package com.moneyware.bank.documentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.moneyware.bank.documentservice.constants.MessageConstants.PLEASE_PROVIDE_PASSWORD;
import static com.moneyware.bank.documentservice.constants.MessageConstants.PLEASE_PROVIDE_USERNAME;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO implements Serializable {


    @NotNull(message = PLEASE_PROVIDE_USERNAME)
    private String username;

    @NotNull(message = PLEASE_PROVIDE_PASSWORD)
    private String password;

}
