package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.user.admin.AdminUserRepository;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    private final AdminUserRepository adminUserRepository;
    private final CustomerRepository customerRepository;
//    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
//        var user = userRepo.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetails(user);
//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Long userId = 0L;
        AdminUser adminUser = adminUserRepository.findByEmail(email).orElse(null);
        if(adminUser!=null){
            adminUser = adminUserRepository.findByMobile(email).orElse(null);
        }
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if(customer!=null){
            customer = customerRepository.findByMobile(email).orElse(null);
        }
        if(adminUser!=null){
            userId=adminUser.getUser().getId();
        }
        if(customer!=null){
            userId=customer.getUser().getId();
        }
        User user = userRepo.findUserWithAllPermissions(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}