package com.osudpotro.posmaster.tms.vechile;

import lombok.Data;

@Data
public class UpdateVehicleRequest {
    private String name;
    private String licenceNo;
    private Long multimediaId;
}
