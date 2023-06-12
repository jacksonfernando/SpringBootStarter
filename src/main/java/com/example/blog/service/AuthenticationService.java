package com.example.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.config.JwtService;
import com.example.blog.dto.request.RegisterRequestDTO;
import com.example.blog.dto.response.AuthenticationResponseDTO;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()));
        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDTO(jwtToken);
    }

    public AuthenticationResponseDTO authenticate(RegisterRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User foundedUser = repository.findByUsername(request.getUsername()).orElseThrow(null);
        String jwtToken = jwtService.generateToken(foundedUser);
        return new AuthenticationResponseDTO(jwtToken);
    }
}