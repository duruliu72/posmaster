package com.osudpotro.posmaster.address.thana;

import com.osudpotro.posmaster.address.district.DistrictDto;
import com.osudpotro.posmaster.address.division.DivisionDto;
import lombok.Data;

@Data
public class ThanaDto {
    private Long id;
    private String name;
    private DistrictDto district;
    private DivisionDto division;
}
