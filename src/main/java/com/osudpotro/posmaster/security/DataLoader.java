package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.action.ActionNotFoundException;
import com.osudpotro.posmaster.action.ActionRepository;
import com.osudpotro.posmaster.resource.Resource;
import com.osudpotro.posmaster.resource.ResourceAction;
import com.osudpotro.posmaster.resource.ResourceDetailsRepository;
import com.osudpotro.posmaster.resource.ResourceRepository;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.user.DuplicateUserException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.user.admin.AdminUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(
            ActionRepository actionRepository,
            RoleRepository roleRepository,
            ResourceRepository resourceRepository,
            UserRepository userRepository,
            AdminUserRepository adminUserRepository,
            PermissionRepository permissionRepository,
            PermissionDetailRepository permissionDetailRepository,
            ResourceDetailsRepository resourceDetailsRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            loadInitialData(
                    actionRepository,
                    roleRepository,
                    resourceRepository,
                    userRepository,
                    adminUserRepository,
                    permissionRepository,
                    permissionDetailRepository,
                    resourceDetailsRepository,
                    passwordEncoder
            );
        };
    }

    @Transactional
    public void loadInitialData(
            ActionRepository actionRepository,
            RoleRepository roleRepository,
            ResourceRepository resourceRepository,
            UserRepository userRepository,
            AdminUserRepository adminUserRepository,
            PermissionRepository permissionRepository,
            PermissionDetailRepository permissionDetailRepository,
            ResourceDetailsRepository resourceDetailsRepository,
            PasswordEncoder passwordEncoder
    ) {
        // === SUPER ADMIN USER ===
        String email = "duruliu72@gmail.com";
        String mobile = "01726720772";
        Optional<User> findUser = userRepository.findByEmail(email);
        User superAdminUser = findUser.orElseGet(() -> {
            if (userRepository.existsByEmail(email)) {
                throw new DuplicateUserException("Email  is already registered");
            }
            if (userRepository.existsByMobile(mobile)) {
                throw new DuplicateUserException("Mobile  is already registered");
            }
            User u = new User();
            u.setUserType(UserType.ADMIN);
            u.setEmail(email);
            u.setMobile(mobile);
            u.setPassword(passwordEncoder.encode("123"));
            AdminUser adminUser = new AdminUser();
            u = userRepository.save(u);
            adminUser.setUser(u);
            adminUserRepository.save(adminUser);
            return u;
        });
        // === ACTIONS ===
        if (actionRepository.count() == 0) {
            Action view = new Action("READ");
            view.setCreatedBy(superAdminUser);
            Action create = new Action("CREATE");
            create.setCreatedBy(superAdminUser);
            Action update = new Action("UPDATE");
            update.setCreatedBy(superAdminUser);
            Action delete = new Action("DELETE");
            delete.setCreatedBy(superAdminUser);
            actionRepository.saveAll(Arrays.asList(
                    view, create, update, delete
            ));
            System.out.println("✅ Actions inserted");
        }
        // === ROLES ===
        Role findSuperAdminRole = roleRepository.findByRoleKey("ROLE_SUPER_ADMIN")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("Super AdminUser");
                    superAdmin.setRoleKey("ROLE_SUPER_ADMIN");
                    superAdmin.setCreatedBy(superAdminUser);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // === RESOURCES ===
        Resource findProductResource = resourceRepository.findByName("Product")
                .orElseGet(() -> {
                    Resource productResource = new Resource();
                    productResource.setName("Product");
                    productResource.setResourceKey("PRODUCT");
                    productResource.setUrl("/products");
                    productResource.setCreatedBy(superAdminUser);
                    Action view = actionRepository.findByName("READ").orElseThrow(() -> new ActionNotFoundException());
                    Action create = actionRepository.findByName("CREATE").orElseThrow(() -> new ActionNotFoundException());
                    ResourceAction resourceActionView = new ResourceAction();
                    resourceActionView.setResource(productResource);
                    resourceActionView.setAction(view);
                    resourceActionView.setChecked(true);
                    resourceActionView.setCreatedBy(superAdminUser);
                    ResourceAction resourceActionCreate = new ResourceAction();
                    resourceActionCreate.setResource(productResource);
                    resourceActionCreate.setAction(create);
                    resourceActionCreate.setChecked(true);
                    resourceActionCreate.setCreatedBy(superAdminUser);
                    productResource.setResourceActions(Arrays.asList(resourceActionView, resourceActionCreate));
                    return resourceRepository.save(productResource);
                });
        Resource findUserResource = resourceRepository.findByName("User")
                .orElseGet(() -> {
                    Resource userUIResource = new Resource();
                    userUIResource.setName("User");
                    userUIResource.setResourceKey("USER");
                    userUIResource.setUrl("/users");
                    userUIResource.setCreatedBy(superAdminUser);
                    Action view = actionRepository.findByName("READ").orElseThrow(() -> new ActionNotFoundException());
                    Action create = actionRepository.findByName("CREATE").orElseThrow(() -> new ActionNotFoundException());
                    ResourceAction resourceActionView = new ResourceAction();
                    resourceActionView.setResource(userUIResource);
                    resourceActionView.setAction(view);
                    resourceActionView.setChecked(true);
                    resourceActionView.setCreatedBy(superAdminUser);
                    ResourceAction resourceActionCreate = new ResourceAction();
                    resourceActionCreate.setResource(userUIResource);
                    resourceActionCreate.setAction(create);
                    resourceActionCreate.setChecked(true);
                    resourceActionCreate.setCreatedBy(superAdminUser);
                    userUIResource.setResourceActions(Arrays.asList(resourceActionView, resourceActionCreate));
                    return resourceRepository.save(userUIResource);
                });
        // ===SET ROLE SUPER ADMIN USER  ===
        superAdminUser.setRoles(Set.of(findSuperAdminRole));
        userRepository.save(superAdminUser);
        // === PERMISSIONS ===
        if (permissionRepository.count() == 0) {
            Permission permission = new Permission();
            permission.setRole(findSuperAdminRole);
            permission.setResource(findProductResource);
            permission.setPermissionType(PermissionType.ROLE);
            permission.setEnable(true);
            permission.setCreatedBy(superAdminUser);
            permission = permissionRepository.save(permission);
            //=== ResourceRequest Details====
            Action read = actionRepository.findByName("READ").orElseThrow(() -> new ActionNotFoundException());
            Action create = actionRepository.findByName("CREATE").orElseThrow(() -> new ActionNotFoundException());
            PermissionDetail detail1 = new PermissionDetail();
            detail1.setPermission(permission);
            detail1.setAction(read);
            detail1.setCreatedBy(superAdminUser);

            PermissionDetail detail2 = new PermissionDetail();
            detail2.setPermission(permission);
            detail2.setAction(create);
            detail2.setCreatedBy(superAdminUser);
            permissionDetailRepository.saveAll(Arrays.asList(detail1, detail2));
            System.out.println("✅ Permissions inserted for Super AdminUser");
        }
    }
}
