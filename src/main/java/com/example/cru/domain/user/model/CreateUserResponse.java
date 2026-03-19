package com.example.cru.domain.user.model;

import com.example.cru.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserResponse {
    private final String email;
    private final String name;
    private final String phon;

    public static CreateUserResponse from(User user) {
        return CreateUserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phon(user.getPhone())
                .build();
    }
}
