package com.osudpotro.posmaster.securityadmistration.module;

import lombok.Data;

@Data
public class ModuleCreateRequest {
    private String moduleCode;
    private String moduleName;
    private String description;
}
