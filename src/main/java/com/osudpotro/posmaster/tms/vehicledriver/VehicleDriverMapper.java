package com.osudpotro.posmaster.tms.vehicledriver;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
import org.springframework.stereotype.Component;

@Component
public class VehicleDriverMapper {
    public VehicleDriverDto toDto(VehicleDriver vehicleDriver) {
        if (vehicleDriver == null) {
            return null;
        }
        VehicleDriverDto vehicleDriverDto = new VehicleDriverDto();
        vehicleDriverDto.setId(vehicleDriver.getId());
        User user =vehicleDriver.getUser();
        vehicleDriverDto.setUserName(user.getUserName());
        vehicleDriverDto.setUserName(user.getUserName());
        vehicleDriverDto.setFirstName(user.getFirstName());
        vehicleDriverDto.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            vehicleDriverDto.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            vehicleDriverDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            vehicleDriverDto.setMobile(user.getMobile());
        }
        if (user.getSecondaryEmail() != null && !user.getSecondaryEmail().isEmpty()) {
            vehicleDriverDto.setSecondaryEmail(user.getSecondaryEmail());
        }
        if (user.getSecondaryMobile() != null && !user.getSecondaryMobile().isEmpty()) {
            vehicleDriverDto.setSecondaryMobile(user.getSecondaryMobile());
        }
        vehicleDriverDto.setGender(user.getGender());
        if (user.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(user.getProfilePic().getId());
            multimediaDto.setName(user.getProfilePic().getName());
            multimediaDto.setImageUrl(user.getProfilePic().getImageUrl());
            vehicleDriverDto.setProfilePic(multimediaDto);
        }
        return vehicleDriverDto;
    }

    public VehicleDriver toEntity(VehicleDriverCreateRequest request) {
        return new VehicleDriver();
    }
    public User toUserEntity(VehicleDriverCreateRequest request) {
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
    void update(UpdateVehicleDriverRequest request, VehicleDriver vehicleDriver) {

    }
    void updateUser(UpdateVehicleDriverRequest request, User user) {
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
