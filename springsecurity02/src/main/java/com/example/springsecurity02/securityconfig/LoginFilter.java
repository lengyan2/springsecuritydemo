package com.example.springsecurity02.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    //自定义前后端人、分离认证filter

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1.是否是post请求
        if (!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("AUthuentication method not supported");
        }
        //2。是否是json
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
        //3。json获取用户输入的用户名和密码惊喜认证
            try {
                Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = map.get(getUsernameParameter());
                String password = map.get(getPasswordParameter());
                System.out.println("用户名："+username+"密码："+password);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request,authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.attemptAuthentication(request,response);
    }
}
