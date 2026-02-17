package com.osudpotro.posmaster.tms.vehicledriver;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class VehicleDriverService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final VehicleDriverRepository vehicleDriverRepository;
    private final VehicleDriverMapper vehicleDriverMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MultimediaRepository multimediaRepository;

    public List<VehicleDriverDto> gerAllVehicleDrivers() {
        return vehicleDriverRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(vehicleDriverMapper::toDto)
                .toList();
    }

    public Page<VehicleDriverDto> getVehicleDrivers(VehicleDriverFilter filter, Pageable pageable) {
        return vehicleDriverRepository.findAll(VehicleDriverSpecification.filter(filter), pageable).map(vehicleDriverMapper::toDto);
    }

    public VehicleDriverDto registerVehicleDriver(VehicleDriverCreateRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateVehicleDriverException("Email already exists");
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateVehicleDriverException("Phone number already exists");
        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            throw new DuplicateVehicleDriverException();
        }
        var vehicleDriver = vehicleDriverMapper.toEntity(request);
        var authUser = authService.getCurrentUser();
        //Common User Entity
        User user = vehicleDriverMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.VEHICLE_DRIVER);
        user.setCreatedBy(authUser);
        vehicleDriver.setCreatedBy(authUser);
        Role findRole = roleRepository.findByRoleKey("ROLE_VEHICLE_DRIVER")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("VehicleDriver");
                    superAdmin.setRoleKey("ROLE_VEHICLE_DRIVER");
                    superAdmin.setCreatedBy(authUser);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // ===SET ROLE ADMIN USER  ===
        user.setRoles(Set.of(findRole));
        user = userRepository.save(user);
        vehicleDriver.setUser(user);
        vehicleDriver.setCreatedBy(authUser);
        vehicleDriverRepository.save(vehicleDriver);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }
    public VehicleDriverDto updateVehicleDriver(Long customerId, UpdateVehicleDriverRequest request) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow(VehicleDriverNotFoundException::new);
        var user = vehicleDriver.getUser();
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new DuplicateVehicleDriverException("Email already exists");
            }
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            if (!user.getMobile().equals(request.getMobile())) {
                throw new DuplicateVehicleDriverException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!user.getEmail().equals(request.getEmail()) && !user.getMobile().equals(request.getMobile())) {
                throw new DuplicateVehicleDriverException();
            }
        }

        vehicleDriverMapper.update(request, vehicleDriver);
        vehicleDriverMapper.updateUser(request, user);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        var authUser = authService.getCurrentUser();
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                user.setProfilePic(multimedia);
            }
        }
        vehicleDriver.setUser(user);
        vehicleDriver.setUpdatedBy(authUser);
        vehicleDriverRepository.save(vehicleDriver);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }
    public VehicleDriverDto getVehicleDriver(Long customerId) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow(VehicleDriverNotFoundException::new);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }

    public VehicleDriverDto getVehicleDriverOrNull(Long customerId) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow();
        return vehicleDriverMapper.toDto(vehicleDriver);
    }

    public VehicleDriver getVehicleDriverEntity(Long customerId) {
        return vehicleDriverRepository.findById(customerId).orElseThrow(VehicleDriverNotFoundException::new);
    }

    public VehicleDriverDto activeVehicleDriver(Long customerId) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow(() -> new VehicleDriverNotFoundException("VehicleDriver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(1);
        vehicleDriver.setUpdatedBy(authUser);
        vehicleDriverRepository.save(vehicleDriver);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }

    public VehicleDriverDto deactivateVehicleDriver(Long customerId) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow(() -> new VehicleDriverNotFoundException("VehicleDriver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(2);
        vehicleDriver.setUpdatedBy(authUser);
        vehicleDriverRepository.save(vehicleDriver);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }

    public VehicleDriverDto deleteVehicleDriver(Long customerId) {
        var vehicleDriver = vehicleDriverRepository.findById(customerId).orElseThrow(() -> new VehicleDriverNotFoundException("VehicleDriver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(3);
        vehicleDriver.setUpdatedBy(authUser);
        vehicleDriverRepository.save(vehicleDriver);
        return vehicleDriverMapper.toDto(vehicleDriver);
    }

    public int deleteBulkVehicleDriver(List<Long> ids) {
        return vehicleDriverRepository.deleteBulkVehicleDriver(ids, 3L);
    }
}
