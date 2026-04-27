package com.osudpotro.posmaster.address.district;

import com.osudpotro.posmaster.address.division.DivisionDto;
import lombok.Data;

@Data
public class DistrictDto {
    private Long id;
    private String name;
    private DivisionDto division;
}
