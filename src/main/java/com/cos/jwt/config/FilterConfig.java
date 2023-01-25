package com.cos.jwt.config;

// 필터 실습을 위한 테스트 코드라 주석 처리 (근데 심지어 적용도 잘 안되었던것 같음 ㅠㅠ 스프링 버전 문제인지..)
/*
// 시큐리티 필터 체인에 있는 필터를 거는게 아니라 내가 필터를 하나 만들어 준다
@Configuration // IOC 등록
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 순서를 정할 수 있는데, 0이라고 하면 우선순위가 가장 높다, 낮은 번호가 필터 중 가장 먼저 실행
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // 순서를 정할 수 있는데, 1이라고 하면 우선순위 0보다 늦게 실행된다
        return bean;
    }
}
*/