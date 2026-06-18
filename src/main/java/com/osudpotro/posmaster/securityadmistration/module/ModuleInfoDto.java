package com.osudpotro.posmaster.securityadmistration.module;

import lombok.Data;

@Data
public class ModuleInfoDto {
    private Long id;
    private String moduleCode;
    private String moduleName;
    private String description;
}
