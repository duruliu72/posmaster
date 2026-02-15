package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.user.Employee.Employee;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.security.Permission;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriver;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private UserType userType; // EMPLOYEE, CUSTOMER, etc.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    // @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<UserRole> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Permission> permissions = new HashSet<>();
    @OneToOne(mappedBy = "user")
    private AdminUser adminUser;
    @OneToOne(mappedBy = "user")
    private Employee employee;
    @OneToOne(mappedBy = "user")
    private Customer customer;
    @OneToOne(mappedBy = "user")
    private VehicleDriver vehicleDriver;

    public String getEmail() {
        if (adminUser != null) return adminUser.getEmail();
        if (employee != null) return employee.getEmail();
        if (customer != null) return customer.getEmail();
        if (vehicleDriver != null) return vehicleDriver.getEmail();
        return null;
    }
    public String getMobile() {
        if (adminUser != null) return adminUser.getMobile();
        if (employee != null) return employee.getMobile();
        if (customer != null) return customer.getMobile();
        if (vehicleDriver != null) return vehicleDriver.getMobile();
        return null;
    }
    public String getPassword() {
        if (adminUser != null) return adminUser.getPassword();
        if (employee != null) return employee.getPassword();
        if (customer != null) return customer.getPassword();
        if (vehicleDriver != null) return vehicleDriver.getPassword();
        return null;
    }
    // Check if this user has the given login ID (email or mobile)
    public boolean hasLoginId(String loginId) {
        return loginId.equals(getEmail()) || loginId.equals(getMobile());
    }
    // Get display name based on user type
    public String getDisplayName() {
        if (adminUser != null) return adminUser.getUserName();
        if (employee != null) return employee.getUserName();
        if (customer != null) return customer.getUserName(); // Assuming customer has name
        if (vehicleDriver != null) return vehicleDriver.getUserName(); // Assuming driver has name
        return "User";
    }
    public boolean isAdmin() {
        return adminUser != null;
    }

    public boolean isCustomer() {
        return customer != null;
    }

    public boolean isVehicleDriver() {
        return vehicleDriver != null;
    }
}


//ALTER TABLE users
//DROP CONSTRAINT users_user_type_check;
//
//ALTER TABLE users
//ADD CONSTRAINT users_user_type_check
//CHECK (user_type IN ('ADMIN','CUSTOMER','STAFF','SUPER_ADMIN'));