package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
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
        User user =adminUser.getUser();
        adminUserDto.setUserName(user.getUserName());
        adminUserDto.setUserName(user.getUserName());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            adminUserDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            adminUserDto.setMobile(user.getMobile());
        }
        if (user.getSecondaryEmail() != null && !user.getSecondaryEmail().isEmpty()) {
            adminUserDto.setSecondaryEmail(user.getSecondaryEmail());
        }
        if (user.getSecondaryMobile() != null && !user.getSecondaryMobile().isEmpty()) {
            adminUserDto.setSecondaryMobile(user.getSecondaryMobile());
        }
        adminUserDto.setGender(user.getGender());
        if (user.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(user.getProfilePic().getId());
            multimediaDto.setName(user.getProfilePic().getName());
            multimediaDto.setImageUrl(user.getProfilePic().getImageUrl());
            adminUserDto.setProfilePic(multimediaDto);
        }
        return adminUserDto;
    }
    public User toUserEntity(AdminUserCreateRequest request) {
        User user = new User();
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            user.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            user.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            user.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            user.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            user.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            user.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            user.setProviderId(request.getProviderId());
        }
        return user;
    }
    public AdminUser toEntity(AdminUserCreateRequest request) {
//        AdminUser adminUser = new AdminUser();
//        return adminUser;
        return new AdminUser();
    }
    void updateUser(UpdateAdminUserRequest request, User user) {
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            user.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            user.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            user.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            user.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            user.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            user.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            user.setProviderId(request.getProviderId());
        }
    }
    void update(UpdateAdminUserRequest request, AdminUser adminUser) {

    }

}
