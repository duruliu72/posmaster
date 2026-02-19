package com.osudpotro.posmaster.user;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.tms.driver.Driver;
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
@Table(name = "users",indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_mobile", columnList = "mobile"),
})
public class User extends BaseEntity {
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = true)
    private String email;
    @Column(unique = true, nullable = true)
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    private Boolean isValidAccount;
    //like Male=1, Female=2, Third=3
    private Integer gender;
    private String provider;
    private String providerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia profilePic;
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
    @OneToOne(mappedBy = "user")

    private AdminUser adminUser;
    @OneToOne(mappedBy = "user")
    private Employee employee;
    @OneToOne(mappedBy = "user")
    private Customer customer;
    @OneToOne(mappedBy = "user")
    private Driver driver;
}