package com.farmover.server.farmover.payloads;

import com.farmover.server.farmover.entities.Role;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String uname;
    private String email;
    private String password;
    private Role role;
    private String phone;
    private String address;

}
