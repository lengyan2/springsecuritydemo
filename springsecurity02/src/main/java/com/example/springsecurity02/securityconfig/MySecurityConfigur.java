package com.example.springsecurity02.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class MySecurityConfigur extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService1;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService1);
    }

    @Override
    @Autowired
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setUsernameParameter("uname");
        loginFilter.setPasswordParameter("passwd");
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationFailureHandler(((request, response, exception) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("msg","登录失败");
            map.put("code",500);
            map.put("原因",exception.getMessage());
            response.setContentType("application/json;charset=utf8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            String s = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(s);
        })); // 认证失败处理
        loginFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("msg","登录成功");
            map.put("code",200);
            map.put("用户信息",authentication.getPrincipal());
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json:charset=GBK");
            String s = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(s);
        })); // 认证成功处理
        return loginFilter;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("kaptcha.jpg").permitAll()
                .anyRequest().authenticated()// 所有请求都要认证
                .and()
                .formLogin()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.ALL_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().println("请认证之后在处理");
                }))
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("msg","注销成功");
                    map.put("code",200);
                    map.put("用户信息",authentication.getPrincipal());
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json:charset=GBK");
                    String s = new ObjectMapper().writeValueAsString(map);
                    response.getWriter().println(s);
                }))
//                .successHandler()
//                .failureHandler();
        .and().csrf().disable();
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
