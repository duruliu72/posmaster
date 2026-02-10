package com.osudpotro.posmaster.auth;

import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.CustomUserMapper;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final CustomUserMapper userMapper;
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userRepository.findUserWithAllPermissions(userDetails.getUser().getId()).orElse(null);
        }
        throw new UnauthorizedException("Invalid authentication principal: " + principal);
    }
}