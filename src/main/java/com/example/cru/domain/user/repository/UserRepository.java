package com.example.cru.domain.user.repository;

import com.example.cru.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
   Optional<User> findById(Long id);

    //중복 이메일 여부
    Boolean existsByEmail(String email);

    //로그인 시 이메일로 회원 조회
    Optional<User> findByEmail(String email);

}
