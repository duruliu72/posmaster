package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.tms.driver.DriverMapper;
import com.osudpotro.posmaster.tms.vechile.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleTripMapper {
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    public VehicleTripDto toDto(VehicleTrip vehicleTrip) {
        if (vehicleTrip == null) {
            return null;
        }
        VehicleTripDto vehicleTripDto = new VehicleTripDto();
        vehicleTripDto.setId(vehicleTrip.getId());
        vehicleTripDto.setTripRef(vehicleTrip.getTripRef());
        if(vehicleTrip.getDriver()!=null){
            vehicleTripDto.setDriver(driverMapper.toDto(vehicleTrip.getDriver()));
        }
        if(vehicleTrip.getVehicle()!=null){
            vehicleTripDto.setVehicle(vehicleMapper.toDto(vehicleTrip.getVehicle()));
        }
        vehicleTripDto.setTripStatus(vehicleTrip.getTripStatus());
        vehicleTripDto.setTripStartTime(vehicleTrip.getTripStartTime());
        vehicleTripDto.setTripEndTime(vehicleTrip.getTripEndTime());
        return vehicleTripDto;
    }
    public VehicleTrip toEntity(VehicleTripCreateRequest request) {
        VehicleTrip vehicleTrip=new VehicleTrip();
        vehicleTrip.setTripRef(request.getTripRef());
//        vehicleTrip.setSourceAddress(request.getSourceAddress());
//        vehicleTrip.setDestAddress(request.getDestAddress());
//        Location source=new Location();
//        source.setLongitude(request.getSourceLongitude());
//        source.setLatitude(request.getDestLatitude());
//        source.setAccuracy(request.getSourceAccuracy());
//        vehicleTrip.setSource(source);
//        Location destination=new Location();
//        destination.setLongitude(request.getDestLongitude());
//        destination.setLatitude(request.getDestLatitude());
//        destination.setAccuracy(request.getDestAccuracy());
//        vehicleTrip.setDestination(destination);
//        vehicleTrip.setDestAddress(request.getDestAddress());
        return vehicleTrip;
    }
    void update(UpdateVehicleTripRequest request, VehicleTrip vehicleTrip) {

    }
}
