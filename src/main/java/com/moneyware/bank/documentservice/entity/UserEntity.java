package com.moneyware.bank.documentservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.moneyware.bank.documentservice.constants.MessageConstants.PLEASE_PROVIDE_NAME;
import static com.moneyware.bank.documentservice.constants.MessageConstants.PLEASE_PROVIDE_PASSWORD;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_details")
public class UserEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "username")
    @NotNull(message = PLEASE_PROVIDE_NAME)
    private String username;

    @Column(name = "password")
    @NotNull(message = PLEASE_PROVIDE_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
