package com.example.cru.domain.user.service;

import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.model.CreateUserRequest;
import com.example.cru.domain.user.model.CreateUserResponse;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User user = new User(
                null,
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getPhone()
        );
        userRepository.save(user);
        return CreateUserResponse.from(user);
    }
}
