package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.picture.PictureDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            customerDto.setEmail(customer.getEmail());
        }
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            customerDto.setEmail(customer.getEmail());
        }
        if (customer.getMobile() != null && !customer.getMobile().isEmpty()) {
            customerDto.setMobile(customer.getMobile());
        }
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customerDto.setPassword(customer.getPassword());
        }
        if (customer.getSecondaryEmail() != null && !customer.getSecondaryEmail().isEmpty()) {
            customerDto.setSecondaryEmail(customer.getSecondaryEmail());
        }
        if (customer.getSecondaryMobile() != null && !customer.getSecondaryMobile().isEmpty()) {
            customerDto.setSecondaryMobile(customer.getSecondaryMobile());
        }
        customerDto.setGender(customer.getGender());
        customerDto.setOtpCode(customer.getOtpCode());
        customerDto.setProvider(customer.getProvider());
        customerDto.setProviderId(customerDto.getProviderId());
        if (customer.getImage() != null) {
            PictureDto pictureDto = new PictureDto();
            pictureDto.setId(customer.getImage().getId());
            pictureDto.setName(customer.getImage().getName());
            pictureDto.setImageUrl(customer.getImage().getImageUrl());
            customerDto.setImage(pictureDto);
        }
        return customerDto;
    }

    public Customer toEntity(CustomerCreateRequest request) {
        Customer customer = new Customer();
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            customer.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            customer.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            customer.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            customer.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            customer.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            customer.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            customer.setProviderId(request.getProviderId());
        }
        if (request.getOtpCode() != null && !request.getOtpCode().isEmpty()) {
            customer.setOtpCode(request.getOtpCode());
        }
        return customer;
    }

    void update(CustomerUpdateRequest request, Customer customer) {
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            customer.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            customer.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            customer.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            customer.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            customer.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            customer.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            customer.setProviderId(request.getProviderId());
        }
        if (request.getOtpCode() != null && !request.getOtpCode().isEmpty()) {
            customer.setOtpCode(request.getOtpCode());
        }
    }
}
