package com.osudpotro.posmaster.tms.vehicletrip;
import com.osudpotro.posmaster.tms.driver.DriverDto;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.vechile.VehicleDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VehicleTripDto {
    private Long id;
    private String tripRef;
    private DriverDto driver;
    private VehicleDto vehicle;
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
    private TripStatus tripStatus;
    private List<GoodsOnTrip> goodsItems;
}
