package com.osudpotro.posmaster.user;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.user.Employee.Employee;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.security.Permission;
import com.osudpotro.posmaster.role.Role;
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Permission> permissions = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @OneToOne(mappedBy = "user")
    private AdminUser adminUser;
    @OneToOne(mappedBy = "user")
    private Employee employee;
    @OneToOne(mappedBy = "user")
    private Customer customer;
    public String getPassword() {
        if(userType.equals(UserType.ADMIN)){
            return adminUser.getPassword();
        }
        if(userType.equals(UserType.EMPLOYEE)){
            return employee.getPassword();
        }
        if(userType.equals(UserType.CUSTOMER)){
            return customer.getPassword();
        }
        return null;
    }
    public String getUserName() {
        if(userType.equals(UserType.ADMIN)){
            return adminUser.getUserName();
        }
        if(userType.equals(UserType.EMPLOYEE)){
            return employee.getUserName();
        }
        if(userType.equals(UserType.CUSTOMER)){
            return customer.getUserName();
        }
        return null;
    }
    public String getEmail() {
        if(userType.equals(UserType.ADMIN)){
            return adminUser.getEmail();
        }
        if(userType.equals(UserType.EMPLOYEE)){
            return employee.getEmail();
        }
        if(userType.equals(UserType.CUSTOMER)){
            return customer.getEmail();
        }
        return null;
    }
    public String getMobile() {
        if(userType.equals(UserType.ADMIN)){
            return adminUser.getMobile();
        }
        if(userType.equals(UserType.EMPLOYEE)){
            return employee.getMobile();
        }
        if(userType.equals(UserType.CUSTOMER)){
            return customer.getMobile();
        }
        return null;
    }
}