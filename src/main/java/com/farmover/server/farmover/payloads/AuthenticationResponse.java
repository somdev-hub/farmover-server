package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String email;
    private String role;

    public AuthenticationResponse(String token,String email,String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
