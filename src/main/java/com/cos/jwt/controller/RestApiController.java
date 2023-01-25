package com.cos.jwt.controller;

import com.cos.jwt.model.JwtUser;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin // 에렇게 cors정책 허용 어노테이션을 걸 수 도 있지만 이렇게 하게 되면 인증이 필요하지 않은 요청에만 적용됨
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody JwtUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    // user, manager, admin 셋 다 접근 가능
    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    // manager, admin만 접근 가능, user 접근 불가
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin만 접근 가능, user, manager 접근 불가
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
