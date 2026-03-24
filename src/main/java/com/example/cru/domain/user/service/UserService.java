package com.example.cru.domain.user.service;

import com.example.cru.domain.user.entity.User;
import com.example.cru.domain.user.model.RegisterUserRequest;
import com.example.cru.domain.user.model.RegisterUserResponse;
import com.example.cru.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 가입 로직
    @Transactional
    public RegisterUserResponse registerUser(RegisterUserRequest request) {

        // 데이터 준비
        // 비밀번호 암호화를 위해 requestPassword -> passwordEncoder 변경
        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), //암호화
//                request.getPassword(),
                request.getName(),
                request.getPhone()
        );

        // 이메일 중복 여부
        Boolean exist = userRepository.existsByEmail(request.getEmail());
        if (exist) {
            throw new RuntimeException("중복된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        // 데이터 저장
        userRepository.save(user);

        //Dto 반환
        return RegisterUserResponse.from(user);
    }
}
