package com.osudpotro.posmaster.resource;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.security.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_resources")
public class ApiResource extends BaseEntity {
    private String name;
    @Column(name = "api_resource_key", nullable = false, unique = true, length = 50)
    private String apiResourceKey;
    @Column(name = "api_url", unique = true)
    private String apiUrl;
//    @OneToMany(mappedBy = "apiResource", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<ResourceRequest> permissions = new HashSet<>();
}