package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.TransactionsDto;
import com.farmover.server.farmover.payloads.UserDto;
import com.farmover.server.farmover.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        UserDto user = userService.getUser(userId);

        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {

        List<UserDto> allUsers = userService.getAllUsers();

        return new ResponseEntity<List<UserDto>>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/transactions/{email}")
    public ResponseEntity<List<TransactionsDto>> getUserTransactions(@PathVariable String email) {
        List<TransactionsDto> transactions = userService.getUserTransactions(email);
        return new ResponseEntity<List<TransactionsDto>>(transactions, HttpStatus.OK);
    }

}
