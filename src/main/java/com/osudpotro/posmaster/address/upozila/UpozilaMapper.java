package com.osudpotro.posmaster.address.upozila;
import com.osudpotro.posmaster.address.district.District;
import com.osudpotro.posmaster.address.district.DistrictDto;
import com.osudpotro.posmaster.address.division.Division;
import com.osudpotro.posmaster.address.division.DivisionDto;
import org.springframework.stereotype.Component;

@Component
public class UpozilaMapper {
    //Mapping Here
    //Entity → DTO
    public UpozilaDto toDto(Upozila upozila) {
        UpozilaDto upozilaDto=new UpozilaDto();
        upozilaDto.setId(upozila.getId());
        upozilaDto.setName(upozila.getName());
        if(upozila.getDivision()!=null){
            Division division=upozila.getDivision();
            DivisionDto divisionDto=new DivisionDto();
            divisionDto.setId(division.getId());
            divisionDto.setName(division.getName());
            upozilaDto.setDivision(divisionDto);
        }
        if(upozila.getDistrict()!=null){
            District district=upozila.getDistrict();
            DistrictDto districtDto=new DistrictDto();
            districtDto.setId(district.getId());
            districtDto.setName(district.getName());
            upozilaDto.setDistrict(districtDto);
        }

      return upozilaDto;
    }
}
