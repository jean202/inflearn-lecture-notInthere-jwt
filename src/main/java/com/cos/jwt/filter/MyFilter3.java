package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 필터 실습을 위한 테스트 코드라 주석 처리 (근데 심지어 적용도 잘 안되었던것 같음 ㅠㅠ 스프링 버전 문제인지..)
/*
public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        System.out.println("MyFilter3.doFilter");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        // cos라는 이름의 token을 만들었다고 가정하자
        // ID, PW가 정상적으로 들어와서 로그인이 완료되면 cos 토큰을 만들고 응답해 준다.
        // 요청할 때마다 header의 Authorization에 value값으로 cos token을 가지고 올것인데
        // 그 때 token이 서버가 만든 token이 맞는지 검증하면 된다. (RSA or HS256)
        // token이 cos로 날라올때만 컨트롤러로 진입을 하게 해주고 그렇지 않다면 진입하지 못하게 막고자 함
        if(req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터3");
            if (headerAuth.equals("cos")) {
                // program이 끝나지 않고 계속 process를 진행시키도록 하기 위해
                filterChain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }
    }
}
*/