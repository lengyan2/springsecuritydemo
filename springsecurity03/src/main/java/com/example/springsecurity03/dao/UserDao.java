package com.example.springsecurity03.dao;

import com.example.springsecurity03.entity.Role;
import com.example.springsecurity03.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.ManagedBean;
import java.util.List;

@Mapper
public interface UserDao {

    // 根据用户名查询用户
    User loadUserByUsername(@Param("username") String username);

    //根据用户id查询用户角色
    List<Role> getRoleListByUid(@Param("uid") Integer uid);
}
