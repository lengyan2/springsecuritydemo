package com.example.springsecurity02.securityconfig;

import com.example.springsecurity02.dao.UserDao;
import com.example.springsecurity02.entity.Role;
import com.example.springsecurity02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) throw new AuthenticationServiceException("用户不存在");
        List<Role> roles = userDao.searchRoleByUid(user.getId());
        user.setList(roles);
        return user;
    }
}
