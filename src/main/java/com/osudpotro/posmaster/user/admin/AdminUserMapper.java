package com.osudpotro.posmaster.user.admin;

import org.springframework.stereotype.Component;


@Component
public class AdminUserMapper {
    //Mapping Here
    //Entity → DTO
    public AdminUserDto toDto(AdminUser adminUser) {
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(adminUser.getId());
        adminUserDto.setFirstName(adminUser.getFirstName());
        adminUserDto.setLastName(adminUser.getLastName());
        adminUserDto.setEmail(adminUser.getEmail());
        adminUserDto.setMobile(adminUser.getMobile());
        adminUserDto.setCreatedAt(adminUser.getCreatedAt());
        return adminUserDto;
    }

    AdminUser toEntity(RegisterAdminUserRequest request) {
        AdminUser adminUser = new AdminUser();
        adminUser.setEmail(request.getEmail());
        adminUser.setMobile(request.getMobile());
        return adminUser;
    }
}
