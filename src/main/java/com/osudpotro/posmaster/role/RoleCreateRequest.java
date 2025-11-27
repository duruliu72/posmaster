package com.osudpotro.posmaster.role;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleCreateRequest {
    private String name;
    private String roleKey;
    private Set<ResourceRequest> resources = new HashSet<>();
}
