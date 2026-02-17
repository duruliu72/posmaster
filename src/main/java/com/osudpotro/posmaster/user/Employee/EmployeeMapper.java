package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
import org.springframework.stereotype.Component;


@Component
public class EmployeeMapper {
    //Mapping Here
    //Entity → DTO
    public EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        User user =employee.getUser();
        employeeDto.setUserName(user.getUserName());
        employeeDto.setUserName(user.getUserName());
        employeeDto.setFirstName(user.getFirstName());
        employeeDto.setLastName(user.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            employeeDto.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            employeeDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            employeeDto.setMobile(user.getMobile());
        }
        if (user.getSecondaryEmail() != null && !user.getSecondaryEmail().isEmpty()) {
            employeeDto.setSecondaryEmail(user.getSecondaryEmail());
        }
        if (user.getSecondaryMobile() != null && !user.getSecondaryMobile().isEmpty()) {
            employeeDto.setSecondaryMobile(user.getSecondaryMobile());
        }
        employeeDto.setGender(user.getGender());
        if (user.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(user.getProfilePic().getId());
            multimediaDto.setName(user.getProfilePic().getName());
            multimediaDto.setImageUrl(user.getProfilePic().getImageUrl());
            employeeDto.setProfilePic(multimediaDto);
        }
        return employeeDto;
    }

    public User toUserEntity(EmployeeCreateRequest request) {
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
    public Employee toEntity(EmployeeCreateRequest request) {
//        AdminUser adminUser = new AdminUser();
//        return adminUser;
        return new Employee();
    }
    void update(UpdateEmployeeRequest request, Employee employee) {

    }
    void updateUser(UpdateEmployeeRequest request, User user) {
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
