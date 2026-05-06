package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.branch.BranchMapper;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AdminUserMapper {
    @Autowired
    private BranchMapper branchMapper;

    //Mapping Here
    //Entity → DTO
    public AdminUserDto toDto(AdminUser adminUser) {
        if (adminUser == null) {
            return null;
        }
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(adminUser.getId());
        User user = adminUser.getUser();
        adminUserDto.setUserName(adminUser.getUserName());
        adminUserDto.setUserName(adminUser.getUserName());
        adminUserDto.setFirstName(adminUser.getFirstName());
        adminUserDto.setLastName(adminUser.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            adminUserDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            adminUserDto.setMobile(user.getMobile());
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
        if (user.getBranch() != null) {
            adminUserDto.setBranch(branchMapper.toDto(user.getBranch()));
        }
        return adminUserDto;
    }

    public User toUserEntity(AdminUserCreateRequest request) {
        User user = new User();
        user.setUserType(UserType.ADMIN);
        return user;
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
        return new AdminUser();
    }

    void updateUser(UpdateAdminUserRequest request, User user) {
//        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
//            user.setUserName(request.getUserName());
//        }
//        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
//            user.setFirstName(request.getFirstName());
//        }
//        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
//            user.setLastName(request.getLastName());
//        }
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            user.setEmail(request.getEmail());
//        }
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            user.setPassword(request.getPassword());
//        }
//        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
//            user.setMobile(request.getMobile());
//        }
//        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
//            user.setSecondaryEmail(request.getSecondaryEmail());
//        }
//        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
//            user.setSecondaryMobile(request.getSecondaryMobile());
//        }
//        if (request.getGender() != null && !request.getGender().equals(0)) {
//            user.setGender(request.getGender());
//        }
//        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
//            user.setProvider(request.getProvider());
//        }
//        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
//            user.setProviderId(request.getProviderId());
//        }
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
    }

}
