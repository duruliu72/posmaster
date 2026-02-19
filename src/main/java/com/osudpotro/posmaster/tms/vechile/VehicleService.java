package com.osudpotro.posmaster.tms.vechile;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VehicleService {
    private final AuthService authService;
    private final VehicleRepository vehicleRepository;
    private final MultimediaRepository multimediaRepository;
    private final  VehicleMapper vehicleMapper;
    public List<VehicleDto> gerAllVehicles() {
        return vehicleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }
    public Page<VehicleDto> filterVehicles(VehicleFilter filter, Pageable pageable) {
        return vehicleRepository.findAll(VehicleSpecification.filter(filter), pageable).map(vehicleMapper::toDto);
    }

    public VehicleDto createVehicle(VehicleCreateRequest request) {
        if (request.getLicenceNo() != null && vehicleRepository.existsByLicenceNo(request.getLicenceNo())) {
            throw new DuplicateVehicleException("Vehicle already exists");
        }
        var authUser = authService.getCurrentUser();
        var vehicle = vehicleMapper.toEntity(request);
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                vehicle.setVehicleDoc(multimedia);
            }
        }
        vehicle.setCreatedBy(authUser);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }
    public VehicleDto updateVehicle(Long vehicleId, UpdateVehicleRequest request) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(VehicleNotFoundException::new);
        if (request.getLicenceNo() != null && vehicleRepository.existsByLicenceNo(request.getLicenceNo())) {
            if (!vehicle.getLicenceNo().equals(request.getLicenceNo())) {
                throw new DuplicateVehicleException("Email already exists");
            }
        }
        vehicleMapper.update(request, vehicle);
        var authUser = authService.getCurrentUser();
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                vehicle.setVehicleDoc(multimedia);
            }
        }
        vehicle.setUpdatedBy(authUser);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }
    public VehicleDto getVehicle(Long vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(VehicleNotFoundException::new);
        return vehicleMapper.toDto(vehicle);
    }

    public VehicleDto getVehicleOrNull(Long vehicleId) {
        var vehicleDriver = vehicleRepository.findById(vehicleId).orElseThrow();
        return vehicleMapper.toDto(vehicleDriver);
    }

    public Vehicle getVehicleEntity(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(VehicleNotFoundException::new);
    }

    public VehicleDto activeVehicle(Long vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));
        var authUser = authService.getCurrentUser();
        vehicle.setStatus(1);
        vehicle.setUpdatedBy(authUser);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    public VehicleDto deactivateVehicle(Long vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));
        var authUser = authService.getCurrentUser();
        vehicle.setStatus(2);
        vehicle.setUpdatedBy(authUser);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    public VehicleDto deleteVehicle(Long vehicleId) {
        var vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));
        var authUser = authService.getCurrentUser();
        vehicle.setStatus(3);
        vehicle.setUpdatedBy(authUser);
        vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    public int deleteBulkVehicle(List<Long> ids) {
        return vehicleRepository.deleteBulkVehicle(ids, 3L);
    }
}
