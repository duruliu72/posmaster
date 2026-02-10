package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.action.Action;
import com.osudpotro.posmaster.action.ActionNotFoundException;
import com.osudpotro.posmaster.action.ActionRepository;
import com.osudpotro.posmaster.resource.api.ApiResource;
import com.osudpotro.posmaster.resource.api.ApiResourceRepository;
import com.osudpotro.posmaster.resource.ui.UiResource;
import com.osudpotro.posmaster.resource.ui.UiResourceAction;
import com.osudpotro.posmaster.resource.ui.UiResourceDetailsRepository;
import com.osudpotro.posmaster.resource.ui.UiResourceRepository;
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
            UiResourceRepository uiResourceRepository,
            ApiResourceRepository apiResourceRepository,
            UserRepository userRepository,
            AdminUserRepository adminUserRepository,
            PermissionRepository permissionRepository,
            PermissionDetailRepository permissionDetailRepository,
            UiResourceDetailsRepository uiResourceDetailsRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            loadInitialData(
                    actionRepository,
                    roleRepository,
                    uiResourceRepository,
                    apiResourceRepository,
                    userRepository,
                    adminUserRepository,
                    permissionRepository,
                    permissionDetailRepository,
                    uiResourceDetailsRepository,
                    passwordEncoder
            );
        };
    }

    @Transactional
    public void loadInitialData(
            ActionRepository actionRepository,
            RoleRepository roleRepository,
            UiResourceRepository uiResourceRepository,
            ApiResourceRepository apiResourceRepository,
            UserRepository userRepository,
            AdminUserRepository adminUserRepository,
            PermissionRepository permissionRepository,
            PermissionDetailRepository permissionDetailRepository,
            UiResourceDetailsRepository uiResourceDetailsRepository,
            PasswordEncoder passwordEncoder
    ) {
        // === SUPER ADMIN USER ===
        String email="duruliu72@gmail.com";
        String mobile="01700000000";
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
            AdminUser adminUser = new AdminUser();
            adminUser.setEmail(email);
            adminUser.setMobile(mobile);
            adminUser.setPassword(passwordEncoder.encode("123"));
            u=userRepository.save(u);
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
        // === UiRESOURCES ===
        UiResource findProductUIResource = uiResourceRepository.findByName("Product")
                .orElseGet(() -> {
                    UiResource productUIResource = new UiResource();
                    productUIResource.setName("Product");
                    productUIResource.setUiResourceKey("PRODUCT");
                    productUIResource.setPageUrl("/products");
                    productUIResource.setCreatedBy(superAdminUser);
                    Action view = actionRepository.findByName("READ").orElseThrow(() -> new ActionNotFoundException());
                    Action create = actionRepository.findByName("CREATE").orElseThrow(() -> new ActionNotFoundException());
                    UiResourceAction uiResourceActionView = new UiResourceAction();
                    uiResourceActionView.setUiResource(productUIResource);
                    uiResourceActionView.setAction(view);
                    uiResourceActionView.setChecked(true);
                    uiResourceActionView.setCreatedBy(superAdminUser);
                    UiResourceAction uiResourceActionCreate = new UiResourceAction();
                    uiResourceActionCreate.setUiResource(productUIResource);
                    uiResourceActionCreate.setAction(create);
                    uiResourceActionCreate.setChecked(true);
                    uiResourceActionCreate.setCreatedBy(superAdminUser);
                    productUIResource.setUiResourceActions(Arrays.asList(uiResourceActionView, uiResourceActionCreate));
                    return uiResourceRepository.save(productUIResource);
                });
        UiResource findUserUiResource = uiResourceRepository.findByName("User")
                .orElseGet(() -> {
                    UiResource userUIResource = new UiResource();
                    userUIResource.setName("User");
                    userUIResource.setUiResourceKey("USER");
                    userUIResource.setPageUrl("/users");
                    userUIResource.setCreatedBy(superAdminUser);
                    Action view = actionRepository.findByName("READ").orElseThrow(() -> new ActionNotFoundException());
                    Action create = actionRepository.findByName("CREATE").orElseThrow(() -> new ActionNotFoundException());
                    UiResourceAction uiResourceActionView = new UiResourceAction();
                    uiResourceActionView.setUiResource(userUIResource);
                    uiResourceActionView.setAction(view);
                    uiResourceActionView.setChecked(true);
                    uiResourceActionView.setCreatedBy(superAdminUser);
                    UiResourceAction uiResourceActionCreate = new UiResourceAction();
                    uiResourceActionCreate.setUiResource(userUIResource);
                    uiResourceActionCreate.setAction(create);
                    uiResourceActionCreate.setChecked(true);
                    uiResourceActionCreate.setCreatedBy(superAdminUser);
                    userUIResource.setUiResourceActions(Arrays.asList(uiResourceActionView, uiResourceActionCreate));
                    return uiResourceRepository.save(userUIResource);
                });

        // === API RESOURCES ===
        ApiResource findApiResourceApi = apiResourceRepository.findByApiUrl("/api/api-resources")
                .orElseGet(() -> {
                    ApiResource productApiResource = new ApiResource();
                    productApiResource.setName("Resource API");
                    productApiResource.setApiResourceKey("RESOURCE");
                    productApiResource.setApiUrl("/api/api-resources");
                    productApiResource.setCreatedBy(superAdminUser);
                    return apiResourceRepository.save(productApiResource);
                });

        ApiResource findUserApi = apiResourceRepository.findByApiUrl("/api/users")
                .orElseGet(() -> {
                    ApiResource userApiResource = new ApiResource();
                    userApiResource.setName("User API");
                    userApiResource.setApiResourceKey("USER");
                    userApiResource.setApiUrl("/api/users");
                    userApiResource.setCreatedBy(superAdminUser);
                    return apiResourceRepository.save(userApiResource);
                });
        // ===SET ROLE SUPER ADMIN USER  ===
        superAdminUser.setRoles(Set.of(findSuperAdminRole));
        userRepository.save(superAdminUser);
        // === PERMISSIONS ===
        if (permissionRepository.count() == 0) {
            Permission permission = new Permission();
            permission.setRole(findSuperAdminRole);
            permission.setApiResource(findApiResourceApi);
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
