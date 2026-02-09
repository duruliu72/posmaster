package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> gerAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public Page<UserMainDto> filterUsers(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filter(filter), pageable).map(customUserMapper::toMainDto);
    }

    public Page<UserMainDto> filterForOrUsers(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filterOr(filter), pageable).map(customUserMapper::toMainDto);
    }

    public UserDto registerUser(RegiterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email  is already registered");
        }
        var authUser = authService.getCurrentUser();
        var user = userMapper.toEntity(request);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedBy(authUser);
        Role findAdminRole = roleRepository.findByRoleKey("ROLE_ADMIN")
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
        user.setRoles(Set.of(findAdminRole));
        user.setUserType(UserType.EMPLOYEE);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
