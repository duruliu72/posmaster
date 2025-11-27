package com.osudpotro.posmaster.auth;

import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.CustomUserMapper;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserDto;
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
            User user=userRepository.findUserWithAllPermissions(userDetails.getUser().getEmail()).orElse(null);
            return user;
        }
        throw new UnauthorizedException("Invalid authentication principal: " + principal);
    }
}