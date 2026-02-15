package com.osudpotro.posmaster.user.Employee;
import org.springframework.stereotype.Component;


@Component
public class EmployeeMapper {
    //Mapping Here
    //Entity → DTO
    public EmployeeDto toDto(Employee adminUser) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(adminUser.getId());
        employeeDto.setUserName(adminUser.getUserName());
        employeeDto.setFirstName(adminUser.getFirstName());
        employeeDto.setLastName(adminUser.getLastName());
        employeeDto.setEmail(adminUser.getEmail());
        employeeDto.setMobile(adminUser.getMobile());
        employeeDto.setUserId(adminUser.getUser().getId());
        employeeDto.setCreatedAt(adminUser.getCreatedAt());
        return employeeDto;
    }

    Employee toEntity(RegisterEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmail(request.getEmail());
        employee.setMobile(request.getMobile());
        return employee;
    }
}
