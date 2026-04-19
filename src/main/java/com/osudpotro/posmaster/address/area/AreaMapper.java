package com.osudpotro.posmaster.address.area;

import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
    //Mapping Here
    //Entity → DTO
    public AreaDto toDto(Area area) {
      AreaDto areaDto=new AreaDto();
      areaDto.setId(area.getId());
      areaDto.setName(areaDto.getName());
      return areaDto;
    }
}
