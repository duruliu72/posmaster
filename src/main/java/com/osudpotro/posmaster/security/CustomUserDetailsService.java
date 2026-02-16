package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriver;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriverRepository;
import com.osudpotro.posmaster.user.Employee.Employee;
import com.osudpotro.posmaster.user.Employee.EmployeeRepository;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.user.admin.AdminUserRepository;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    private final AdminUserRepository adminUserRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final VehicleDriverRepository vehicleDriverRepository;
    @Override
    public UserDetails loadUserByUsername(String principalWithUserType) throws UsernameNotFoundException {
        String[] parts = principalWithUserType.split("-");
        String email = "";
        String userType = "";
        if (parts.length > 1) {
            email = parts[0];
            userType = parts[1];
        }
        Long userId = 0L;
        if (userType.equalsIgnoreCase(String.valueOf(UserType.ADMIN))) {
            AdminUser adminUser = adminUserRepository.findByEmail(email).orElse(null);
            if (adminUser == null) {
                adminUser = adminUserRepository.findByMobile(email).orElse(null);
            }
            if (adminUser != null) {
                userId = adminUser.getUser().getId();
            }
        }
        if (userType.equalsIgnoreCase(String.valueOf(UserType.EMPLOYEE))) {
            Employee employee= employeeRepository.findByEmail(email).orElse(null);
            if (employee == null) {
                employee = employeeRepository.findByMobile(email).orElse(null);
            }
            if (employee != null) {
                userId = employee.getUser().getId();
            }
        }
        if (userType.equalsIgnoreCase(String.valueOf(UserType.VEHICLE_DRIVER))) {
            VehicleDriver vehicleDriver = vehicleDriverRepository.findByEmail(email).orElse(null);
            if (vehicleDriver == null) {
                vehicleDriver = vehicleDriverRepository.findByMobile(email).orElse(null);
            }
            if (vehicleDriver != null) {
                userId = vehicleDriver.getUser().getId();
            }
        }
        if (userType.equalsIgnoreCase(String.valueOf(UserType.CUSTOMER))) {
            Customer customer = customerRepository.findByEmail(email).orElse(null);
            if (customer == null) {
                customer = customerRepository.findByMobile(email).orElse(null);
            }
            if (customer != null) {
                userId = customer.getUser().getId();
            }
        }
        if (userType.equalsIgnoreCase("userId")) {
            try {
                userId = Long.parseLong(email);
            } catch (NumberFormatException e) {
                System.out.println("Subject is not a number: " + email);
            }
        }
        User user = userRepo.findUserWithAllPermissions(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}