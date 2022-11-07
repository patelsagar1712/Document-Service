package com.moneyware.bank.documentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public final class LoginResponseDTO {
    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;
}

