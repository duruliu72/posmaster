package com.osudpotro.posmaster.role;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleUpdateRequest {
    private String name;
    private Set<ResourceRequest> resources = new HashSet<>();
}
