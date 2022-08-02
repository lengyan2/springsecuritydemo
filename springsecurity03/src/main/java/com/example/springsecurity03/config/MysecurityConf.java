package com.example.springsecurity03.config;

import com.example.springsecurity03.filter.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MysecurityConf extends WebSecurityConfigurerAdapter {



    private final UserDetailsService userDetailsService;
    private final RedisTemplate redisTemplate;


    @Autowired
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(this.redisTemplate);
        loginFilter.setFilterProcessesUrl("/login"); // 设置登录认证的url
        loginFilter.setUsernameParameter("uname");
        loginFilter.setPasswordParameter("passwd");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","登录成功");
            map.put("登录信息",authentication.getPrincipal());
            String s = new ObjectMapper().writeValueAsString(map);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().println(s);

        }));
        loginFilter.setAuthenticationFailureHandler(((request, response, exception) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("code",401);
            map.put("message","登录失败,请检查信息");
            String s = new ObjectMapper().writeValueAsString(map);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(s);
        }));
        return loginFilter;
    }

    @Autowired
    public MysecurityConf(UserDetailsService userDetailsService1,RedisTemplate redisTemplate){
        this.userDetailsService = userDetailsService1;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    @Autowired
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启权限认证
        http.authorizeHttpRequests()
                .mvcMatchers("/kaptcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("code",200);
                    map.put("message","注销成功");
                    String s = new ObjectMapper().writeValueAsString(map);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    response.getWriter().println(s);

                }))
                .and()
                .csrf().disable();
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
