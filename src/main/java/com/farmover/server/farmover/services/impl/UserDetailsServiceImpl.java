package com.farmover.server.farmover.services.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Role;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.repositories.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), getAuthorities(user.getRole()));

        // return userRepo.findByEmail(email)
        //         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Collection<GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

}
