package com.osudpotro.posmaster.role;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.security.Permission;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Column(nullable = false, unique = true, length = 100)
    private String name; // e.g., "Super Admin", "Admin", "Sales Manager"
    @Column(name = "role_key", nullable = false, unique = true, length = 50)
    private String roleKey; // e.g., ROLE_SUPER_ADMIN, ROLE_SALES
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserRole> users = new ArrayList<>();
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Permission> permissions = new HashSet<>();
}