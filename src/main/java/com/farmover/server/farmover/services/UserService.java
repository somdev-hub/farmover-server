package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.TransactionsDto;
import com.farmover.server.farmover.payloads.UserDto;
import com.farmover.server.farmover.payloads.request.UpdateUserRequestDto;

public interface UserService {
    // UserDto registerUser(UserDto userDto);

    UserDto getUser(Integer id);

    UserDto updateUser(UpdateUserRequestDto userDto, String email);

    void deleteUser(Integer id);

    UserDto getUserByEmail(String email);

    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    List<TransactionsDto> getUserTransactions(String email);

}
