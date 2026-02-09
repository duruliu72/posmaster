package com.osudpotro.posmaster.user;
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
public class User  extends BaseEntity {
    private String userName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String mobile;
    private String password;
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
    private Customer customer;
    @OneToOne(mappedBy = "user")
    private VehicleDriver vehicleDriver;
}