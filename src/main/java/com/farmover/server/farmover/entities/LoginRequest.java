package com.farmover.server.farmover.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {
    private String email;
    private String password;
}
