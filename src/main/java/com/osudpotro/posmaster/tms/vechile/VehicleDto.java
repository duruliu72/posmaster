package com.osudpotro.posmaster.tms.vechile;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import lombok.Data;

@Data
public class VehicleDto {
    private Long id;
    private String name;
    private String licenceNo;
    private MultimediaDto vehicleDoc;
}
