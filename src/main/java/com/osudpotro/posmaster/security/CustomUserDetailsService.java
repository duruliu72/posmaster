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
//    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
//        var user = userRepo.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetails(user);
//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = userRepo.findUserWithAllPermissions(userByEmail.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}