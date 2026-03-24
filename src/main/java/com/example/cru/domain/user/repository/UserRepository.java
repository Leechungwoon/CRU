package com.example.cru.domain.user.repository;

import com.example.cru.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
   Optional<User> findById(Long id);

    //중복 이메일 여부
    Boolean existsByEmail(String email);
}
