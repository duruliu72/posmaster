package com.osudpotro.posmaster.address.division;

import org.springframework.stereotype.Component;

@Component
public class DivisionMapper {
    //Mapping Here
    //Entity → DTO
    public DivisionDto toDto(Division division) {
        DivisionDto divisionDto=new DivisionDto();
        divisionDto.setId(division.getId());
        divisionDto.setName(division.getName());
      return divisionDto;
    }
}
