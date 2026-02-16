package com.osudpotro.posmaster.user.Employee;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeBulkUpdateRequest {
    private List<Long> employeeIds;
}
