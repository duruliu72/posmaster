package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
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
        employeeDto.setUserName(employee.getUserName());
        employeeDto.setUserName(employee.getUserName());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            employeeDto.setEmail(employee.getEmail());
        }
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            employeeDto.setEmail(employee.getEmail());
        }
        if (employee.getMobile() != null && !employee.getMobile().isEmpty()) {
            employeeDto.setMobile(employee.getMobile());
        }
        if (employee.getSecondaryEmail() != null && !employee.getSecondaryEmail().isEmpty()) {
            employeeDto.setSecondaryEmail(employee.getSecondaryEmail());
        }
        if (employee.getSecondaryMobile() != null && !employee.getSecondaryMobile().isEmpty()) {
            employeeDto.setSecondaryMobile(employee.getSecondaryMobile());
        }
        employeeDto.setGender(employee.getGender());
        if (employee.getProfilePic() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(employee.getProfilePic().getId());
            multimediaDto.setName(employee.getProfilePic().getName());
            multimediaDto.setImageUrl(employee.getProfilePic().getImageUrl());
            employeeDto.setProfilePic(multimediaDto);
        }
        return employeeDto;
    }

    public Employee toEntity(EmployeeCreateRequest request) {
        Employee employee = new Employee();
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            employee.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            employee.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            employee.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            employee.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            employee.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            employee.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            employee.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            employee.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            employee.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            employee.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            employee.setProviderId(request.getProviderId());
        }
        return employee;
    }

    void update(UpdateEmployeeRequest request, Employee employee) {
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            employee.setUserName(request.getUserName());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            employee.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            employee.setEmail(request.getEmail());
        }
        if (request.getEmail() == null) {
            employee.setIsValidEmail(false);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(request.getPassword());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            employee.setMobile(request.getMobile());
        }
        if (request.getSecondaryEmail() != null && !request.getSecondaryEmail().isEmpty()) {
            employee.setSecondaryEmail(request.getSecondaryEmail());
        }
        if (request.getSecondaryMobile() != null && !request.getSecondaryMobile().isEmpty()) {
            employee.setSecondaryMobile(request.getSecondaryMobile());
        }
        if (request.getGender() != null && !request.getGender().equals(0)) {
            employee.setGender(request.getGender());
        }
        if (request.getProvider() != null && !request.getProvider().isEmpty()) {
            employee.setProvider(request.getProvider());
        }
        if (request.getProviderId() != null && !request.getProviderId().isEmpty()) {
            employee.setProviderId(request.getProviderId());
        }
    }
}
