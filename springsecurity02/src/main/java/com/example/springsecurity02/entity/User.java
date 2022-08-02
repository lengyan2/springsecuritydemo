package com.example.springsecurity02.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data

public class User implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<Role> list = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       Set<GrantedAuthority> grantedAuthorityList = new HashSet<>();
       list.forEach(role -> {
           SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
           grantedAuthorityList.add(simpleGrantedAuthority);
       });
       return grantedAuthorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
