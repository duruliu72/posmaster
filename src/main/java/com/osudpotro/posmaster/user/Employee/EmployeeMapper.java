package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.branch.BranchMapper;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmployeeMapper {
    @Autowired
    private BranchMapper branchMapper;
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
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            employeeDto.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            employeeDto.setEmail(user.getEmail());
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            employeeDto.setMobile(user.getMobile());
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
        if(user.getBranch()!=null){
            employeeDto.setBranch(branchMapper.toDto(user.getBranch()));
        }
        return employeeDto;
    }

    public User toUserEntity(EmployeeCreateRequest request) {
        User user = new User();
        user.setUserType(UserType.EMPLOYEE);
        return user;
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
    void updateUser(UpdateEmployeeRequest request, User user) {

    }

}
