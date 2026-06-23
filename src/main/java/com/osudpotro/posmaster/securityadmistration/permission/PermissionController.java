package com.osudpotro.posmaster.securityadmistration.permission;

import com.osudpotro.posmaster.securityadmistration.resource.Resource;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final ResourceService resourceService;
    private final PermissionService permissionService;
    @GetMapping("/resources")
    public List<PermissionResourceDto> getAllPermissionResources() {
        return permissionService.getAllPermissionResources();
    }

}
