package com.cos.jwt.controller;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.Users;
import com.cos.jwt.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin // 에렇게 cors정책 허용 어노테이션을 걸 수 도 있지만 이렇게 하게 되면 인증이 필요하지 않은 요청에만 적용됨
@RestController
@RequestMapping("api/v2")
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 모든 사람이 접근 가능한 메서드
    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

//    @PostMapping("token")
//    public String token() {
//        return "<h1>token</h1>";
//    }

    // 추가설명 ????
    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal를 사용하는 것이 불가능.
    // @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.


    @PostMapping("joinUser")
    public String joinUser(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    @PostMapping("joinManager")
    public String joinManager(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_MANAGER");
        userRepository.save(user);
        return "회원가입완료";
    }

    @PostMapping("joinAdmin")
    public String joinAdmin(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_ADMIN");
        userRepository.save(user);
        return "회원가입완료";
    }


    // user, manager, admin 셋 다 접근 가능
    @GetMapping("user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : " + principal.getUser().getId());
        System.out.println("principal : " + principal.getUser().getUsername());
        System.out.println("principal : " + principal.getUser().getPassword());

        return "<h1>user</h1>";
    }

    // manager, admin만 접근 가능, user 접근 불가
    @GetMapping("manager/reports")
    public String managerReports() {
        System.out.println("RestApiController.manager");
        return "<h1>reports</h1>";
    }

    // admin만 접근 가능, user, manager 접근 불가
    @GetMapping("admin/users")
    public List<Users> adminUsers() {
        return userRepository.findAll();
    }
}
