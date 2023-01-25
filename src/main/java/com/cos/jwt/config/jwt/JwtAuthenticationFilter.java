package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// login 요청해서 usernamee, password를 전송하면 이 필터가 동작한다
// 그런데 SecurityConfig에 formLogin을 disable해놨기 때문에 동작지 않는다
// 동작하게 하기 위해서는 .addFilter()로 달아주면 된다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication 로그인 시도중");

//        username과 password를 받아서 정상인지 아닌지 로그인 시도를 해본다
//        authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출된다 : loadUserByUsername이 자동으로 실행됨
//        굳이 PrincipalDetails를 세션에 담는 이유 : 담지 않으면 권한 관리가 안된다 (ROLE_XXX등)

        // 1. username, password 받기
        // 2. authenticationManager로 로그인 시도
        // 3. PrincipalDetails의 loadByUsername() 실행
        // 4. PrincipalDetails를 세션에 담기
        // 5. JWT토큰을 만들어서 응답해준다

        // 1.
        try {                 // 이 byte안에 username과 password가 담겨있다
            System.out.println(request.getInputStream().toString());
            ObjectMapper mapper = new ObjectMapper();
            Users user = mapper.readValue(request.getInputStream(), Users.class);

            // formLogin방식이 아니기 때문에 직접 토큰을 만들어 준다
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // password는 spring security가 알아서 처리해 주고, username만 체크하면 된다?

            // 2. 3.
            // 이 코드가 실행되면 PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            // : authentication에 내 로그인한 정보가 담기게 된다
            // DB에 있는 username, password와 일치하는지 확인
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 값이 있으면 로그인 정상적으로 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            // 4.
            // authentication객체를 session영역에 저장 => 로그인이 되었다는 뜻
            // 굳이 jwt 토큰을 사용하면서 세션을 만들 이유가 없으나 권한 처리를 해야 해서 세션에 넣어준다
            // 리턴만 해주면 권한 관리를 security가 대신 해준다

            // 5.
            // 그런데, 여기서 굳이 JWT토큰을 만들지 않아도 된다
            // ->
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return null; 강사님은 이거 해주라 했는데, 빨간줄 에러남
    }

    // attemptAuthentication메서드가 종료된 후, 인증이 정상적으로 되었으면 그 뒤에 실행되는 메서드
    // JWT토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response 해 주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.successfulAuthentication : 인증은 완료되었다는 뜻");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // HMAC + Sha 방식 ?
        String jwtToken = JWT.create()
                                        // 토큰이름
            .withSubject(principalDetails.getUsername())
                                                            // 만료 시간
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
            // 비공개 클레임, 내가 넣고 싶은 값을 키-밸류 형식으 뭐든 막 넣으면 된다..?
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("username", principalDetails.getUser().getUsername())
            // 시크릿은 서버만 아는 고유한 값이어야(이 값으로 싸인을 하는것)
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        // 사용자에게 응답할 response 헤더에 Bearer값으로 내려줌
    }
}
