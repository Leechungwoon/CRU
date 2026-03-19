package com.example.cru.domain.user.model;

import lombok.Getter;

@Getter

public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private String phon;
}
