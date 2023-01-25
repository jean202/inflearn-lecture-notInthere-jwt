package com.cos.jwt.config.auth;

import com.cos.jwt.model.Users;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login 요청(스프링 시큐리티의 기본 로그인 요청 url)이 올 때 등장한다
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername");
        Users user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
