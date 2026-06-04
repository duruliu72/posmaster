package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.address.area.Area;
import com.osudpotro.posmaster.user.customer.Customer;
import org.springframework.stereotype.Component;


@Component
public class AddressMapper {
    //Mapping Here
    //Entity → DTO
    public AddressDto toDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        if(address.getUser()!=null){
            addressDto.setUserId(address.getUser().getId());
        }
        if(address.getCustomer()!=null){
            Customer customer=address.getCustomer();
            addressDto.setCustomerId(customer.getId());
            addressDto.setCustomerName(customer.getUserName());
        }
        addressDto.setFullName(address.getFullName());
        addressDto.setEmail(address.getEmail());
        addressDto.setMobile(address.getMobile());
        addressDto.setAddressType(address.getAddressType());
        addressDto.setAddressCategory(address.getAddressCategory());
        if(address.getArea()!=null){
            Area area=address.getArea();
            addressDto.setAreaId(area.getId());
            addressDto.setAreaName(area.getName());
        }
        addressDto.setPlaceId(address.getPlaceId());
        addressDto.setLatitude(address.getLatitude());
        addressDto.setLongitude(address.getLongitude());
        addressDto.setAccuracy(address.getAccuracy());
        addressDto.setLocationName(address.getLocationName());
        addressDto.setLocationDesc(address.getLocationDesc());
        addressDto.setIsDefault(address.getIsDefault());
        return addressDto;
    }

    public Address toEntity(AddressCreateRequest request) {
        Address address = new Address();
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            address.setFullName(request.getFullName());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            address.setMobile(request.getMobile());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            address.setEmail(request.getEmail());
        }
        if (request.getAddressType() != null) {
            address.setAddressType(request.getAddressType());
        }
        if (request.getAddressCategory() != null) {
            address.setAddressCategory(request.getAddressCategory());
        }
        if(request.getPlaceId()!=null){
            address.setPlaceId(request.getPlaceId());
        }
        if (request.getLatitude() != null) {
            address.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            address.setLongitude(request.getLongitude());
        }
        if (request.getAccuracy() != null) {
            address.setAccuracy(request.getAccuracy());
        }
        if (request.getLocationDesc() != null && !request.getLocationDesc().isEmpty()) {
            address.setLocationDesc(request.getLocationDesc());
        }
        if (request.getLocationName() != null && !request.getLocationName().isEmpty()) {
            address.setLocationName(request.getLocationName());
        }
        if(request.getIsDefault()!=null){
            address.setIsDefault(request.getIsDefault());
        }
        return address;
    }
    void update(AddressUpdateRequest request, Address address) {
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            address.setFullName(request.getFullName());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            address.setMobile(request.getMobile());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            address.setEmail(request.getEmail());
        }
        if (request.getAddressType() != null) {
            address.setAddressType(request.getAddressType());
        }
        if (request.getAddressCategory() != null) {
            address.setAddressCategory(request.getAddressCategory());
        }
        if(request.getPlaceId()!=null){
            address.setPlaceId(request.getPlaceId());
        }
        if (request.getLatitude() != null) {
            address.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            address.setLongitude(request.getLongitude());
        }
        if (request.getAccuracy() != null) {
            address.setAccuracy(request.getAccuracy());
        }
        if (request.getLocationDesc() != null && !request.getLocationDesc().isEmpty()) {
            address.setLocationDesc(request.getLocationDesc());
        }
        if (request.getLocationName() != null && !request.getLocationName().isEmpty()) {
            address.setLocationName(request.getLocationName());
        }
        if(request.getIsDefault()!=null){
            address.setIsDefault(request.getIsDefault());
        }
    }
}