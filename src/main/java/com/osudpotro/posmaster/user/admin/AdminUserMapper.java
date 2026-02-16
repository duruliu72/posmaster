package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import org.springframework.stereotype.Component;


@Component
public class AdminUserMapper {
    //Mapping Here
    //Entity → DTO
    public AdminUserDto toDto(AdminUser adminUser) {
        if (adminUser == null) {
            return null;
        }
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(adminUser.getId());
        adminUserDto.setUserName(adminUser.getUserName());
        adminUserDto.setUserName(adminUser.getUserName());
        adminUserDto.setFirstName(adminUser.getFirstName());
        adminUserDto.setLastName(adminUser.getLastName());
        if (adminUser.getEmail() != null && !adminUser.getEmail().isEmpty()) {
            adminUserDto.setEmail(adminUser.getEmail());
        }
        if (adminUser.getEmail() != null && !adminUser.getEmail().isEmpty()) {
            adminUserDto.setEmail(adminUser.getEmail());
        }
        if (adminUser.getMobile() != null && !adminUser.getMobile().isEmpty()) {
            adminUserDto.setMobile(adminUser.getMobile());
        }
        if (adminUser.getSecondaryEmail() != null && !adminUser.getSecondaryEmail().isEmpty()) {
            adminUserDto.setSecondaryEmail(adminUser.getSecondaryEmail());
        }
        if (adminUser.getSecondaryMobile() != null && !adminUser.getSecondaryMobile().isEmpty()) {
            adminUserDto.setSecondaryMobile(adminUser.getSecondaryMobile());
        }
        adminUserDto.setGender(adminUser.getGender());
        if (adminUser.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(adminUser.getProfilePic().getId());
            multimediaDto.setName(adminUser.getProfilePic().getName());
            multimediaDto.setImageUrl(adminUser.getProfilePic().getImageUrl());
            adminUserDto.setProfilePic(multimediaDto);
        }
        return adminUserDto;
    }

    public AdminUser toEntity(AdminUserCreateRequest request) {
        AdminUser adminUser = new AdminUser();
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            adminUser.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            adminUser.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            adminUser.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            adminUser.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            adminUser.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            adminUser.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            adminUser.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            adminUser.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            adminUser.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            adminUser.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            adminUser.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            adminUser.setProviderId(request.getProviderId());
        }
        return adminUser;
    }

    void update(UpdateAdminUserRequest request, AdminUser adminUser) {
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            adminUser.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            adminUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            adminUser.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            adminUser.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            adminUser.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            adminUser.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            adminUser.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            adminUser.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            adminUser.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            adminUser.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            adminUser.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            adminUser.setProviderId(request.getProviderId());
        }
    }
}
