package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        Long userId = null;
        try {
            userId = Long.parseLong(principal);
        } catch (NumberFormatException e) {
            System.out.println("Subject is not a number: " + principal);
        }
        User user = userRepo.findUserWithAllPermissions(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}