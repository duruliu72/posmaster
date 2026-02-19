package com.osudpotro.posmaster.tms.driver;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    public DriverDto toDto(Driver driver) {
        if (driver == null) {
            return null;
        }
        DriverDto driverDto = new DriverDto();
        driverDto.setId(driver.getId());
        User user = driver.getUser();
        driverDto.setUserName(user.getUserName());
        driverDto.setUserName(user.getUserName());
        driverDto.setFirstName(user.getFirstName());
        driverDto.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            driverDto.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            driverDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            driverDto.setMobile(user.getMobile());
        }
        if (user.getSecondaryEmail() != null && !user.getSecondaryEmail().isEmpty()) {
            driverDto.setSecondaryEmail(user.getSecondaryEmail());
        }
        if (user.getSecondaryMobile() != null && !user.getSecondaryMobile().isEmpty()) {
            driverDto.setSecondaryMobile(user.getSecondaryMobile());
        }
        driverDto.setGender(user.getGender());
        if (user.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(user.getProfilePic().getId());
            multimediaDto.setName(user.getProfilePic().getName());
            multimediaDto.setImageUrl(user.getProfilePic().getImageUrl());
            driverDto.setProfilePic(multimediaDto);
        }
        return driverDto;
    }

    public Driver toEntity(DriverCreateRequest request) {
        return new Driver();
    }
    public User toUserEntity(DriverCreateRequest request) {
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
    void update(UpdateDriverRequest request, Driver driver) {

    }
    void updateUser(UpdateDriverRequest request, User user) {
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
}
