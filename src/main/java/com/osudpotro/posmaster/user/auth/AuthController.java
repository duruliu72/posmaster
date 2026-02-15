package com.osudpotro.posmaster.user.auth;


import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriver;
import com.osudpotro.posmaster.tms.vehicledriver.VehicleDriverRepository;
import com.osudpotro.posmaster.user.*;
import com.osudpotro.posmaster.user.Employee.Employee;
import com.osudpotro.posmaster.user.Employee.EmployeeRepository;
import com.osudpotro.posmaster.user.admin.AdminUser;
import com.osudpotro.posmaster.user.admin.AdminUserRepository;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleDriverRepository vehicleDriverRepository;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final CustomUserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody LoginRequest request, HttpServletResponse response) {
        String principal;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            principal = request.getEmail();
        } else if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            principal = request.getMobile();
        } else {
            throw new IllegalArgumentException("Either email or mobile must be provided");
        }
        User user = null;
        if (request.getUserType() == 1) {
            //For Admin
            String principalWithUserType = String.format("%s-%s", principal, UserType.ADMIN);
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principalWithUserType, request.getPassword()));
            AdminUser adminUser = null;
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // Search by EMAIL only
                adminUser = adminUserRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));
            }
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                // Search by EMAIL only
                adminUser = adminUserRepository.findByMobile(request.getMobile())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getMobile()));
            }
            if (adminUser == null) {
                throw new UserNotFoundException("User not found");
            }
            user=adminUser.getUser();
        }
        if (request.getUserType() == 2) {
            //For Employee
            String principalWithUserType = String.format("%s-%s", principal, UserType.EMPLOYEE);
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principalWithUserType, request.getPassword()));
            Employee employee = null;
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // Search by EMAIL only
                employee = employeeRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));
            }
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                // Search by EMAIL only
                employee = employeeRepository.findByMobile(request.getMobile())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getMobile()));
            }
            if (employee == null) {
                throw new UserNotFoundException("User not found");
            }
            user=employee.getUser();
        }
        if (request.getUserType() == 3) {
            //For Vehicle Driver
            String principalWithUserType = String.format("%s-%s", principal, UserType.VEHICLE_DRIVER);
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principalWithUserType, request.getPassword()));
            VehicleDriver vehicleDriver = null;
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                // Search by EMAIL only
                vehicleDriver = vehicleDriverRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getEmail()));
            }
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                // Search by EMAIL only
                vehicleDriver = vehicleDriverRepository.findByMobile(request.getMobile())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User not found with email: " + request.getMobile()));
            }
            if (vehicleDriver == null) {
                throw new UserNotFoundException("User not found");
            }
            user=vehicleDriver.getUser();
        }
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);
        return jwt.isExpired();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        User user = authService.getCurrentUser();
        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}


//For Post Man
//var jsonData = JSON.parse(responseBody);
//postman.setEnvironmentVariable("accessToken", "Bearer "+jsonData.token)