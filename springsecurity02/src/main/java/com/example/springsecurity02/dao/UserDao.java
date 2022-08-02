package com.example.springsecurity02.dao;

import com.example.springsecurity02.entity.Role;
import com.example.springsecurity02.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserDao {

    // 根据用户名查询user信息
    User loadUserByUsername(@Param("username") String username);

    // 根据uid查询roles
    List<Role> searchRoleByUid(@Param("uid") Integer uid);
}
