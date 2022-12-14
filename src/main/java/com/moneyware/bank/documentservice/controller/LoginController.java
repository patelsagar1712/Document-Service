package com.moneyware.bank.documentservice.controller;

import com.moneyware.bank.documentservice.dto.LoginRequestDTO;
import com.moneyware.bank.documentservice.dto.LoginResponseDTO;
import com.moneyware.bank.documentservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody @Valid LoginRequestDTO authenticationRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
