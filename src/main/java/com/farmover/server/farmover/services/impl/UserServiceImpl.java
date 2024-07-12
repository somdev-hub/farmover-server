package com.farmover.server.farmover.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.TransactionsDto;
import com.farmover.server.farmover.payloads.UserDto;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto getUser(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "user id", Integer.toString(id));
        });

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "user id", Integer.toString(id));
        });

        user.setUname(userDto.getUname());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "user id", Integer.toString(id));
        });

        userRepo.delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "user id", Integer.toString(id));
        });

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<UserDto> users = userRepo.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).toList();

        return users;
    }

    @Override
    public List<TransactionsDto> getUserTransactions(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        List<Transactions> transactions = user.getTransactions();

        List<TransactionsDto> transactionsDtos = transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionsDto.class)).toList();

        return transactionsDtos;
    }

}
