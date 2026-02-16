package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        // Role authorities
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getRoleKey())));
        // Role permissions
        user.getRoles().forEach(role ->
                role.getPermissions().forEach(permission ->
                        permission.getPermissionDetails().forEach(pd ->
                                        authorities.add(new SimpleGrantedAuthority(
                                                permission.getApiResource().getApiResourceKey() + "_" + pd.getAction().getName()
//                                        permission.getUiResource().getUiResourceKey()+"_"+pd.getApiResourceKey().getResourceKey() + "_" + pd.getAction().getName()
                                        ))
                        )));
//         Direct user permissions
        user.getPermissions().forEach(permission ->
                permission.getPermissionDetails().forEach(pd ->
                                authorities.add(new SimpleGrantedAuthority(
                                        permission.getApiResource().getApiResourceKey() + "_" + pd.getAction().getName()
//                                permission.getUiResource().getUiResourceKey()+"_"+pd.getApiResourceKey().getResourceKey() + "_" + pd.getAction().getName()
                                ))
                ));
        return authorities;
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() {
        // Prefer email if available, otherwise return mobile
        String email = user.getEmail();
        if (email != null && !email.isEmpty()) {
            return email;
        }
        String mobile = user.getMobile();
        if (mobile != null && !mobile.isEmpty()) {
            return mobile;
        }
        throw new IllegalStateException("No email or mobile found for user ID: " + user.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1;
    }

    public User getUser() {
        return user;
    }
    public String getEmail() {
        return user.getEmail();
    }

    public String getMobile() {
        return user.getMobile();
    }
}