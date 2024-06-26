package com.farmover.server.farmover.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.AuthenticationResponse;
import com.farmover.server.farmover.payloads.UserDto;
import com.farmover.server.farmover.repositories.UserRepo;

import lombok.Data;

@Service
@Data
public class AuthServiceImpl {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;

    public AuthenticationResponse register(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        user.setUname(userDto.getUname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());

        User savedUser = userRepo.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthenticationResponse(token, savedUser.getEmail(), savedUser.getRole().name());
    }

    public AuthenticationResponse authenticate(UserDto request) {

        User user = modelMapper.map(request, User.class);

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        User byUsername = userRepo.findByEmail(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("user", "username " + user.getUsername(), 0));

        String token = jwtService.generateToken(byUsername);

        return new AuthenticationResponse(
                token,
                byUsername.getEmail(),
                byUsername.getRole().name());
    }
}
