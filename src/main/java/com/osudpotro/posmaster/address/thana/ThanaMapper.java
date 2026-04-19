package com.osudpotro.posmaster.address.thana;
import com.osudpotro.posmaster.address.district.District;
import com.osudpotro.posmaster.address.district.DistrictDto;
import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.address.division.DivisionDto;
import org.springframework.stereotype.Component;

@Component
public class ThanaMapper {
    //Mapping Here
    //Entity → DTO
    public ThanaDto toDto(Thana thana) {
        ThanaDto thanaDto=new ThanaDto();
        thanaDto.setId(thana.getId());
        thanaDto.setName(thana.getName());
        if(thana.getDistrict()!=null){
            District district=thana.getDistrict();
            DistrictDto districtDto=new DistrictDto();
            districtDto.setId(district.getId());
            districtDto.setName(district.getName());
            thanaDto.setDistrict(districtDto);
        }
        if(thana.getDivision()!=null){
            Division division=thana.getDivision();
            DivisionDto divisionDto=new DivisionDto();
            divisionDto.setId(division.getId());
            divisionDto.setName(division.getName());
            thanaDto.setDivision(divisionDto);
        }
      return thanaDto;
    }
}
