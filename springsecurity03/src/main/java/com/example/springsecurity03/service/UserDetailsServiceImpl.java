package com.example.springsecurity03.service;

import com.example.springsecurity03.dao.UserDao;
import com.example.springsecurity03.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final UserDao userDao ;

    @Autowired
    public UserDetailsServiceImpl( UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 拿到数据库中的user
        User user = userDao.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户不存在");
        user.setRoleList(userDao.getRoleListByUid(user.getId()));
        return  user;
    }
}
