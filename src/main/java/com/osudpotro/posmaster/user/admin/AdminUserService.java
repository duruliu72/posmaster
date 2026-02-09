package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.user.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class AdminUserService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUserDto> getAllAdminUsers() {
        return adminUserRepository.findAll()
                .stream()
                .map(adminUserMapper::toDto)
                .toList();
    }

    @Transactional
    public AdminUserDto registerAdminUser(RegisterAdminUserRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email  is already registered");
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateUserException("Mobile  is already registered");
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setEmail(request.getEmail());
        adminUser.setMobile(request.getMobile());
        adminUser.setPassword(passwordEncoder.encode(request.getPassword()));
        var authUser = authService.getCurrentUser();
//        Common User Entity
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setUserType(UserType.ADMIN);
        user.setCreatedBy(authUser);

        Role findRole = roleRepository.findByRoleKey("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("AdminUser");
                    superAdmin.setRoleKey("ROLE_ADMIN");
                    superAdmin.setCreatedBy(authUser);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // ===SET ROLE ADMIN USER  ===
        user.setRoles(Set.of(findRole));
        user=userRepository.save(user);
        adminUser.setUser(user);
        adminUser.setCreatedBy(authUser);
        adminUserRepository.save(adminUser);
        return adminUserMapper.toDto(adminUser);
    }
}
