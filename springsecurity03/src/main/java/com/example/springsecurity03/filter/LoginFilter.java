package com.example.springsecurity03.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    public static  final String SPRING_SECURITY_FORM_KAPTCHA_KEY = "kaptcha";

    private String KAPTCHA_KEY = SPRING_SECURITY_FORM_KAPTCHA_KEY;

    public String getKAPTCHA_KEY() {
        return KAPTCHA_KEY;
    }

    public void setKAPTCHA_KEY(String KEPTCHA_KEY) {
        this.KAPTCHA_KEY = KEPTCHA_KEY;
    }

    private RedisTemplate redisTemplate;

    @Autowired
    public LoginFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 请求的方式是否为post
        if (!request.getMethod().equalsIgnoreCase("POST")) throw new AuthenticationServiceException("请求方式不一致");
        // 请求的数据是否为json

            if (request.getContentType().equalsIgnoreCase("application/json")) {
                // 验证是否成功
                //1. 先验证验证码是否正确
                String Redis_kaptcha = (String) redisTemplate.opsForValue().get("kaptcha");

                ServletInputStream inputStream = null;
                try {
                    inputStream = request.getInputStream();
                    Map<String, String> map = new ObjectMapper().readValue(inputStream, Map.class);
                    String kaptcha = map.get(getKAPTCHA_KEY());

                    if ((Redis_kaptcha != null && kaptcha != null && Redis_kaptcha.equals(kaptcha))) {
                        String username = map.get(getUsernameParameter());
                        String password = map.get(getPasswordParameter());
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                        setDetails(request,authenticationToken);
                        return this.getAuthenticationManager().authenticate(authenticationToken);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                return super.attemptAuthentication(request,response);
        }

    }

