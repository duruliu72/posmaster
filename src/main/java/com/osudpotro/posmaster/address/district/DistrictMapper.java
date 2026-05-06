package com.osudpotro.posmaster.address.district;

import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.address.division.DivisionDto;
import org.springframework.stereotype.Component;

@Component
public class DistrictMapper {
    //Mapping Here
    //Entity → DTO
    public DistrictDto toDto(District district) {
        DistrictDto districtDto=new DistrictDto();
        districtDto.setId(district.getId());
        districtDto.setName(district.getName());
        if(district.getDivision()!=null){
            Division division=district.getDivision();
            DivisionDto divisionDto=new DivisionDto();
            divisionDto.setId(division.getId());
            divisionDto.setName(division.getName());
            districtDto.setDivision(divisionDto);
        }
      return districtDto;
    }
}
