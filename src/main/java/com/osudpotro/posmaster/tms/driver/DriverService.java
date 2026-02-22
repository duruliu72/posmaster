package com.osudpotro.posmaster.tms.driver;

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
public class DriverService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MultimediaRepository multimediaRepository;

    public List<DriverDto> gerAllVehicleDrivers() {
        return driverRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(driverMapper::toDto)
                .toList();
    }

    public Page<DriverDto> getVehicleDrivers(DriverFilter filter, Pageable pageable) {
        return driverRepository.findAll(DriverSpecification.filter(filter), pageable).map(driverMapper::toDto);
    }

    public DriverDto registerDriver(DriverCreateRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateDriverException("Email already exists");
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateDriverException("Phone number already exists");
        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            throw new DuplicateDriverException();
        }
        var vehicleDriver = driverMapper.toEntity(request);
        var authUser = authService.getCurrentUser();
        //Common User Entity
        User user = driverMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.VEHICLE_DRIVER);
        user.setCreatedBy(authUser);
        vehicleDriver.setCreatedBy(authUser);
        Role findRole = roleRepository.findByRoleKey("ROLE_DRIVER")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("Driver");
                    superAdmin.setRoleKey("ROLE_DRIVER");
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
        driverRepository.save(vehicleDriver);
        return driverMapper.toDto(vehicleDriver);
    }
    public DriverDto updateDriver(Long customerId, UpdateDriverRequest request) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow(DriverNotFoundException::new);
        var user = vehicleDriver.getUser();
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new DuplicateDriverException("Email already exists");
            }
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            if (!user.getMobile().equals(request.getMobile())) {
                throw new DuplicateDriverException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!user.getEmail().equals(request.getEmail()) && !user.getMobile().equals(request.getMobile())) {
                throw new DuplicateDriverException();
            }
        }

        driverMapper.update(request, vehicleDriver);
        driverMapper.updateUser(request, user);
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
        driverRepository.save(vehicleDriver);
        return driverMapper.toDto(vehicleDriver);
    }
    public DriverDto getVehicleDriver(Long customerId) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow(DriverNotFoundException::new);
        return driverMapper.toDto(vehicleDriver);
    }

    public DriverDto getVehicleDriverOrNull(Long customerId) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow();
        return driverMapper.toDto(vehicleDriver);
    }

    public Driver getVehicleDriverEntity(Long customerId) {
        return driverRepository.findById(customerId).orElseThrow(DriverNotFoundException::new);
    }

    public DriverDto activeDriver(Long customerId) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(1);
        vehicleDriver.setUpdatedBy(authUser);
        driverRepository.save(vehicleDriver);
        return driverMapper.toDto(vehicleDriver);
    }

    public DriverDto deactivateDriver(Long customerId) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(2);
        vehicleDriver.setUpdatedBy(authUser);
        driverRepository.save(vehicleDriver);
        return driverMapper.toDto(vehicleDriver);
    }

    public DriverDto deleteDriver(Long customerId) {
        var vehicleDriver = driverRepository.findById(customerId).orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(3);
        vehicleDriver.setUpdatedBy(authUser);
        driverRepository.save(vehicleDriver);
        return driverMapper.toDto(vehicleDriver);
    }

    public int deleteBulkVehicle(List<Long> ids) {
        return driverRepository.deleteBulkDriver(ids, 3L);
    }
}
