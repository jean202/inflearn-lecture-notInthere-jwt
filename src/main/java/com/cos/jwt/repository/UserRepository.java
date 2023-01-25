package com.cos.jwt.repository;

import com.cos.jwt.model.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JwtUser, Long> {

    public JwtUser findByUsername(String username);
}
