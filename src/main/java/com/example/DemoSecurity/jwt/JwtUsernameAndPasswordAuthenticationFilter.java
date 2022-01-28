package com.example.DemoSecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;  // inbuilt

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
        Authentication authentication= new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        return authenticate;
    } catch (IOException e){
    throw  new RuntimeException(e);
        }
    }

    // generate token and send it to user


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String key = "securesecuresecuresecuresecuresecuresecuresecuresecuresecure";

        // creating jwt
        String token = Jwts.builder()
                .setSubject(authResult.getName())  // passing name to build payload of jwt
                .claim("authorities", authResult.getAuthorities()) // passing authorities like write and read
                .setIssuedAt(new Date())// now
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .compact();

        // sending it as response to user
        response.addHeader("Authorization" , "Bearer " + token);

    }
}
