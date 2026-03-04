package com.osudpotro.posmaster.tms.vehicletrip;

import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.tms.driver.DriverNotFoundException;
import com.osudpotro.posmaster.tms.driver.DriverRepository;
import com.osudpotro.posmaster.tms.vechile.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class VehicleTripService {
    private final AuthService authService;
    private final VehicleTripRepository vehicleTripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final VehicleTripMapper vehicleTripMapper;

    public List<VehicleTripDto> getAllVehicleTrips() {
        return vehicleTripRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(vehicleTripMapper::toDto)
                .toList();
    }

    public Page<VehicleTripDto> filterVehicleTrips(VehicleTripFilter filter, Pageable pageable) {
        return vehicleTripRepository.findAll(VehicleTripSpecification.filter(filter), pageable).map(vehicleTripMapper::toDto);
    }

    public VehicleTripDto createVehicleTrip(VehicleTripCreateRequest request) {
        String tripRef=generateTripRef();
        if (vehicleTripRepository.existsByTripRef(tripRef)) {
            throw new DuplicateVehicleException("Vehicle Trip already exists");
        }
        var vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(VehicleNotFoundException::new);
        var driver = driverRepository.findById(request.getDriverId()).orElseThrow(DriverNotFoundException::new);
        var authUser = authService.getCurrentUser();
        VehicleTrip vehicleTrip=new VehicleTrip();
        vehicleTrip.setTripRef(generateTripRef());
        vehicleTrip.setVehicle(vehicle);
        vehicleTrip.setDriver(driver);
        vehicleTrip.setCreatedBy(authUser);
        vehicleTripRepository.save(vehicleTrip);
        return vehicleTripMapper.toDto(vehicleTrip);
    }

    public VehicleTripDto updateVehicleTrip(Long vehicleTripId, UpdateVehicleTripRequest request) {
        var vehicleTrip = vehicleTripRepository.findById(vehicleTripId).orElseThrow(VehicleTripNotFoundException::new);
        if (request.getTripRef() != null && vehicleTripRepository.existsByTripRef(request.getTripRef())) {
            if (!vehicleTrip.getTripRef().equals(request.getTripRef())) {
                throw new DuplicateVehicleException("Vehicle Trip already exists");
            }
        }
        var authUser = authService.getCurrentUser();
        TripStatus tripStatus = TripStatus.valueOf(request.getTripStatus());
        vehicleTrip.setTripStatus(tripStatus);
        vehicleTrip.setUpdatedBy(authUser);
        vehicleTripRepository.save(vehicleTrip);
        return vehicleTripMapper.toDto(vehicleTrip);
    }

    public VehicleTripDto getVehicleTrip(Long vehicleTripId) {
        var vehicleTrip = vehicleTripRepository.findById(vehicleTripId).orElseThrow(VehicleTripNotFoundException::new);
        return vehicleTripMapper.toDto(vehicleTrip);
    }

    public VehicleTrip getVehicleTripEntity(Long vehicleId) {
        return vehicleTripRepository.findById(vehicleId).orElseThrow(VehicleTripNotFoundException::new);
    }

    public VehicleTripDto activeVehicleTrip(Long vehicleTripId) {
        var vehicle = vehicleTripRepository.findById(vehicleTripId).orElseThrow(() -> new VehicleTripNotFoundException("Vehicle Trip not found with ID: " + vehicleTripId));
        var authUser = authService.getCurrentUser();
        vehicle.setStatus(1);
        vehicle.setUpdatedBy(authUser);
        vehicleTripRepository.save(vehicle);
        return vehicleTripMapper.toDto(vehicle);
    }

    public VehicleTripDto deactivateVehicleTrip(Long vehicleTripId) {
        var vehicleTrip = vehicleTripRepository.findById(vehicleTripId).orElseThrow(() -> new VehicleNotFoundException("Vehicle Trip not found with ID: " + vehicleTripId));
        var authUser = authService.getCurrentUser();
        vehicleTrip.setStatus(2);
        vehicleTrip.setUpdatedBy(authUser);
        vehicleTripRepository.save(vehicleTrip);
        return vehicleTripMapper.toDto(vehicleTrip);
    }

    public VehicleTripDto deleteVehicleTrip(Long vehicleId) {
        var vehicle = vehicleTripRepository.findById(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle Trip not found with ID: " + vehicleId));
        var authUser = authService.getCurrentUser();
        vehicle.setStatus(3);
        vehicle.setUpdatedBy(authUser);
        vehicleTripRepository.save(vehicle);
        return vehicleTripMapper.toDto(vehicle);
    }

    public int deleteBulkVehicleTrip(List<Long> ids) {
        return vehicleTripRepository.deleteBulkVehicleTrip(ids, 3L);
    }
    public String generateTripRef() {
        VehicleTrip vehicleTrip = vehicleTripRepository.findTopByOrderByCreatedAtDesc();
        if (vehicleTrip == null) {
            vehicleTrip = new VehicleTrip();
        }
        return vehicleTrip.getGeneratedTripRef();
    }
}
