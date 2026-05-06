package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.user.User;
import org.springframework.stereotype.Component;
@Component
public class CustomerMapper {
    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        User user = customer.getUser();
        customerDto.setUserName(customer.getUserName());
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
        customerDto.setProvider(customer.getProvider());
        customerDto.setProviderId(customerDto.getProviderId());
        if (customer.getProfilePic() != null) {
            Multimedia pictureDto = new Multimedia();
            pictureDto.setId(customer.getProfilePic().getId());
            pictureDto.setName(customer.getProfilePic().getName());
            pictureDto.setImageUrl(customer.getProfilePic().getImageUrl());
            customerDto.setProfilePic(pictureDto);
        }
        return customerDto;
    }

    public Customer toEntity(CustomerCreateRequest request) {
        Customer customer = new Customer();
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            customer.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            customer.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
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
        return customer;
    }

    public User toUserEntity(CustomerCreateRequest request) {
        User user = new User();
        return user;
    }

    void update(CustomerUpdateRequest request, Customer customer) {
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            customer.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            customer.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
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
    }

    void updateUser(CustomerUpdateRequest request, User user) {

    }
}
