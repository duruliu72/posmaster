package com.osudpotro.posmaster.address.upozila;

import com.osudpotro.posmaster.address.district.DistrictDto;
import com.osudpotro.posmaster.address.division.DivisionDto;
import lombok.Data;

@Data
public class UpozilaDto {
    private Long id;
    private String name;
    private DivisionDto division;
    private DistrictDto district;
}
