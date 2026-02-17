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
        customerDto.setUserName(user.getUserName());
        customerDto.setFirstName(user.getFirstName());
        customerDto.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            customerDto.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            customerDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            customerDto.setMobile(user.getMobile());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            customerDto.setPassword(user.getPassword());
        }
        if (user.getSecondaryEmail() != null && !user.getSecondaryEmail().isEmpty()) {
            customerDto.setSecondaryEmail(user.getSecondaryEmail());
        }
        if (user.getSecondaryMobile() != null && !user.getSecondaryMobile().isEmpty()) {
            customerDto.setSecondaryMobile(user.getSecondaryMobile());
        }
        customerDto.setGender(user.getGender());
        customerDto.setProvider(user.getProvider());
        customerDto.setProviderId(customerDto.getProviderId());
        if (user.getProfilePic() != null) {
            Multimedia pictureDto = new Multimedia();
            pictureDto.setId(user.getProfilePic().getId());
            pictureDto.setName(user.getProfilePic().getName());
            pictureDto.setImageUrl(user.getProfilePic().getImageUrl());
            customerDto.setProfilePic(pictureDto);
        }
        return customerDto;
    }

    public Customer toEntity(CustomerCreateRequest request) {
        return new Customer();
    }

    public User toUserEntity(CustomerCreateRequest request) {
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

    void update(CustomerUpdateRequest request, Customer customer) {

    }

    void updateUser(CustomerUpdateRequest request, User user) {
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
