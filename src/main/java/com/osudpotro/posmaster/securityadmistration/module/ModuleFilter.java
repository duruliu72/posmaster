package com.osudpotro.posmaster.securityadmistration.module;

import lombok.Data;

@Data
public class ModuleFilter {
    private String moduleCode;
    private String moduleName;
    private String description;
    private Integer status;
}
