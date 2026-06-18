package com.osudpotro.posmaster.securityadmistration.module;

import org.springframework.stereotype.Component;

@Component
public class ModuleInfoMapper {
    //Mapping Here
    //Entity → DTO
    public ModuleInfoDto toDto(ModuleInfo moduleinfo) {
        ModuleInfoDto moduleInfoDto = new ModuleInfoDto();
        moduleInfoDto.setId(moduleinfo.getId());
        moduleInfoDto.setModuleName(moduleinfo.getModuleName());
        moduleInfoDto.setModuleCode(moduleinfo.getModuleCode());
        moduleInfoDto.setDescription(moduleinfo.getDescription());
        return moduleInfoDto;
    }
}
