package com.moneyware.bank.documentservice.security;

import com.moneyware.bank.documentservice.constants.MessageConstants;
import com.moneyware.bank.documentservice.service.impl.UserDetailJWTServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING;
import static com.moneyware.bank.documentservice.constants.MessageConstants.JWT_TOKEN_HAS_EXPIRED;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailJWTServiceImpl userDetailJWTService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println(MessageConstants.UNABLE_TO_GET_JWT_TOKEN);
            } catch (ExpiredJwtException e) {

                System.out.println(JWT_TOKEN_HAS_EXPIRED);
            }
        } else {
            logger.warn(JWT_TOKEN_DOES_NOT_BEGIN_WITH_BEARER_STRING);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailJWTService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
