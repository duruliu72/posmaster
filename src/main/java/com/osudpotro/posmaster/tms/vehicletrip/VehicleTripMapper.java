package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.tms.driver.DriverMapper;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripDto;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripMapper;
import com.osudpotro.posmaster.tms.vechile.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleTripMapper {
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private GoodsOnTripMapper goodsOnTripMapper;
    public VehicleTripDto toDto(VehicleTrip vehicleTrip) {
        if (vehicleTrip == null) {
            return null;
        }
        VehicleTripDto vehicleTripDto = new VehicleTripDto();
        vehicleTripDto.setId(vehicleTrip.getId());
        vehicleTripDto.setTripRef(vehicleTrip.getTripRef());
        if (vehicleTrip.getDriver() != null) {
            vehicleTripDto.setDriver(driverMapper.toDto(vehicleTrip.getDriver()));
        }
        if (vehicleTrip.getVehicle() != null) {
            vehicleTripDto.setVehicle(vehicleMapper.toDto(vehicleTrip.getVehicle()));
        }
        vehicleTripDto.setTripStatus(vehicleTrip.getTripStatus());
        vehicleTripDto.setTripStartTime(vehicleTrip.getTripStartTime());
        vehicleTripDto.setTripEndTime(vehicleTrip.getTripEndTime());
        List<GoodsOnTripDto> list = new ArrayList<>();
        if (vehicleTrip.getGoodsOnTrips() != null) {
            for (var goodsOnTrip : vehicleTrip.getGoodsOnTrips()) {
                GoodsOnTripDto goodsOnTripDto = goodsOnTripMapper.toDto(goodsOnTrip);
                list.add(goodsOnTripDto);
            }
        }
        vehicleTripDto.setGoodsOnTrips(list);
        return vehicleTripDto;
    }
}
