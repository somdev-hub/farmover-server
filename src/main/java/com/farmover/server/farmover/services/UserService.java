package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.TransactionsDto;
import com.farmover.server.farmover.payloads.UserDto;

public interface UserService {
    // UserDto registerUser(UserDto userDto);

    UserDto getUser(Integer id);

    UserDto updateUser(UserDto userDto, Integer id);

    void deleteUser(Integer id);

    UserDto getUserByEmail(String email);

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    List<TransactionsDto> getUserTransactions(String email);

}
