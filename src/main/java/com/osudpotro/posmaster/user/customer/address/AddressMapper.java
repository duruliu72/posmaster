package com.osudpotro.posmaster.user.customer.address;

import org.springframework.stereotype.Component;


@Component
public class AddressMapper {
    //Mapping Here
    //Entity → DTO
    public AddressDto toDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setName(address.getName());
        addressDto.setEmail(address.getEmail());
        addressDto.setMobile(address.getMobile());
        addressDto.setAddressType(address.getAddressType());
        addressDto.setAddressCategory(address.getAddressCategory());
        addressDto.setLatitude(address.getLatitude());
        addressDto.setLongitude(address.getLongitude());
        addressDto.setAccuracy(address.getAccuracy());
        addressDto.setLocationName(address.getLocationName());
        addressDto.setLocationDesc(address.getLocationDesc());
        addressDto.setIsDefault(address.getIsDefault());
        return addressDto;
    }
}