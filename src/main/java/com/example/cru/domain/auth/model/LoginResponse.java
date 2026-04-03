package com.example.cru.domain.auth.model;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long id;
    private final String token;
    private final String refreshToken;

    public LoginResponse(Long id, String token, String refreshToken) {
        this.id = id;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
