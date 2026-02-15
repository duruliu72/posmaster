package com.osudpotro.posmaster.tms.vehicledriver;

import com.osudpotro.posmaster.user.auth.JwtResponse;
import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.user.CustomUserMapper;
import com.osudpotro.posmaster.user.UserNotFoundException;
import com.osudpotro.posmaster.user.UserType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/vehicle-drivers/auth")
public class VehicleDriverAuthController {
    private final JwtService jwtService;
    private final VehicleDriverRepository vehicleDriverRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final CustomUserMapper userMapper;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody VehicleDriverLoginRequest request, HttpServletResponse response) {
        String principal;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            principal = request.getEmail();
        } else if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            principal = request.getMobile();
        } else {
            throw new IllegalArgumentException("Either email or mobile must be provided");
        }
        String principalWithUserType=String.format("%s-%s", principal, UserType.VEHICLE_DRIVER);
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principalWithUserType, request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
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
        if(vehicleDriver==null){
            throw new UserNotFoundException("User not found");
        }
        var accessToken = jwtService.generateAccessToken(vehicleDriver.getUser());
        var refreshToken = jwtService.generateRefreshToken(vehicleDriver.getUser());
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }
}
