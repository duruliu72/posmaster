package com.osudpotro.posmaster.tms.vechile;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import org.springframework.stereotype.Component;
@Component
public class VehicleMapper {
    public VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setName(vehicle.getName());
        vehicleDto.setLicenceNo(vehicle.getLicenceNo());
        if (vehicle.getVehicleDoc() != null) {
            MultimediaDto multimediaDto = new MultimediaDto();
            multimediaDto.setId(vehicle.getVehicleDoc().getId());
            multimediaDto.setName(vehicle.getVehicleDoc().getName());
            multimediaDto.setImageUrl(vehicle.getVehicleDoc().getImageUrl());
            vehicleDto.setVehicleDoc(multimediaDto);
        }
        return vehicleDto;
    }

    public Vehicle toEntity(VehicleCreateRequest request) {
        Vehicle vehicle=new Vehicle();
        if (request.getName() != null && !request.getName().isEmpty()) {
            vehicle.setName(request.getName());
        }
        if (request.getLicenceNo() != null && !request.getLicenceNo().isEmpty()) {
            vehicle.setLicenceNo(request.getLicenceNo());
        }
        return vehicle;
    }
    void update(UpdateVehicleRequest request, Vehicle vehicle) {
        if (request.getName() != null && !request.getName().isEmpty()) {
            vehicle.setName(request.getName());
        }
        if (request.getLicenceNo() != null && !request.getLicenceNo().isEmpty()) {
            vehicle.setLicenceNo(request.getLicenceNo());
        }
    }
}
