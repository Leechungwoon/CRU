package com.example.cru.domain.auth.model;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long id;
    private final String token;

    public LoginResponse(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
