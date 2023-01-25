package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

// IOC할 수 있게 만들기
@Configuration
// 해당 시큐리티를 활성화 시켜 주기
@EnableWebSecurity
// DI 시켜주기
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final CorsConfig corsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 필터 실습을 위한 테스트 코드라 주석 처리 (근데 심지어 적용도 잘 안되었던것 같음 ㅠㅠ 스프링 버전 문제인지..)
/*
        // 그런데, 시큐리티 필터밖에 못받아들이는데 MyFilter1은 javax.servlet.Filter 타입이라 등록이 안된다
        // 굳이 걸고 싶으면 addFilterBefore나 addFilterAfter로 적용시켜 준다
        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class); // 그런데 필터 걸 때에는 이렇게 시큐리티필터에 굳이 걸어줄 필요 없고 따로 걸어줄 수도 있
*/

        http
            // 모든 요청이 이 필터를 타게 됨, 서버가 cors정책에서 벗어날 수 있게 된다 (크로스 오리진 요청이 와도 다 허용)
            .addFilter(corsConfig.corsFilter())
            // session을 사용하지 않겠다, 무상태 스타일의 서버로 만들겠다(둘이 같은 말이라고 함)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 그냥 만들기만 하는건 의미없고, 필터를 꼭 걸어줘야 한다
            // formLogin안쓰겠다 : jwt서버니까 id pw를 입력할 때 form 방식을 사용하지 않겠다
            .formLogin().disable()
            // 기본적 http 연결 방식 안쓰겠다
            .httpBasic().disable()
            // 달아주면 반드시 전달해 줘야 하는 파라미터가 있다 : AuthenticationManager(이 녀석을 통해서 로그인을 진행하기 때문에)
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
            .authorizeRequests()
            // 해당 url 들로 들어오면 접근권한을 이러이러하게 주겠다
            .antMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/manager/**")
            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')")
            // 다른 요청은 다 권한 없이 들어갈 수 있게 설정
            .anyRequest().permitAll();
    }
}
