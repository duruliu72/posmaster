package com.osudpotro.posmaster.tms.vehicledriver;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import org.springframework.stereotype.Component;

@Component
public class VehicleDriverMapper {
    public VehicleDriverDto toDto(VehicleDriver vehicleDriver) {
        if (vehicleDriver == null) {
            return null;
        }
        VehicleDriverDto vehicleDriverDto = new VehicleDriverDto();
        vehicleDriverDto.setId(vehicleDriver.getId());
        vehicleDriverDto.setFirstName(vehicleDriver.getFirstName());
        vehicleDriverDto.setLastName(vehicleDriver.getLastName());
        if (vehicleDriver.getEmail() != null && !vehicleDriver.getEmail().isEmpty()) {
            vehicleDriverDto.setEmail(vehicleDriver.getEmail());
        }
        if (vehicleDriver.getEmail() != null && !vehicleDriver.getEmail().isEmpty()) {
            vehicleDriverDto.setEmail(vehicleDriver.getEmail());
        }
        if (vehicleDriver.getMobile() != null && !vehicleDriver.getMobile().isEmpty()) {
            vehicleDriverDto.setMobile(vehicleDriver.getMobile());
        }
//        if (vehicleDriver.getPassword() != null && !vehicleDriver.getPassword().isEmpty()) {
//            vehicleDriverDto.setPassword(vehicleDriver.getPassword());
//        }
        if (vehicleDriver.getSecondaryEmail() != null && !vehicleDriver.getSecondaryEmail().isEmpty()) {
            vehicleDriverDto.setSecondaryEmail(vehicleDriver.getSecondaryEmail());
        }
        if (vehicleDriver.getSecondaryMobile() != null && !vehicleDriver.getSecondaryMobile().isEmpty()) {
            vehicleDriverDto.setSecondaryMobile(vehicleDriver.getSecondaryMobile());
        }
        vehicleDriverDto.setGender(vehicleDriver.getGender());
        vehicleDriverDto.setProvider(vehicleDriver.getProvider());
        vehicleDriverDto.setProviderId(vehicleDriverDto.getProviderId());
        if (vehicleDriver.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(vehicleDriver.getProfilePic().getId());
            multimediaDto.setName(vehicleDriver.getProfilePic().getName());
            multimediaDto.setImageUrl(vehicleDriver.getProfilePic().getImageUrl());
            vehicleDriverDto.setProfilePic(multimediaDto);
        }
        return vehicleDriverDto;
    }

    public VehicleDriver toEntity(VehicleDriverCreateRequest request) {
        VehicleDriver vehicleDriver = new VehicleDriver();
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            vehicleDriver.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            vehicleDriver.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            vehicleDriver.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            vehicleDriver.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            vehicleDriver.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            vehicleDriver.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            vehicleDriver.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            vehicleDriver.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            vehicleDriver.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            vehicleDriver.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            vehicleDriver.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            vehicleDriver.setProviderId(request.getProviderId());
        }
        return vehicleDriver;
    }

    void update(VehicleDriverUpdateRequest request, VehicleDriver vehicleDriver) {
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            vehicleDriver.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            vehicleDriver.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            vehicleDriver.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            vehicleDriver.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            vehicleDriver.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            vehicleDriver.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            vehicleDriver.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            vehicleDriver.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            vehicleDriver.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            vehicleDriver.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            vehicleDriver.setProviderId(request.getProviderId());
        }
    }
}
