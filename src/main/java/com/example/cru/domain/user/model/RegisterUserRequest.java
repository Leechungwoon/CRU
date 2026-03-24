package com.example.cru.domain.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter

public class RegisterUserRequest {
    private String email;
    private String password;
    private String name;
    private String phone;

    @JsonCreator
    public RegisterUserRequest(
            @JsonProperty("email")String email,
            @JsonProperty("password")String password,
            @JsonProperty("name")String name,
            @JsonProperty("phone")String phone
    ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
