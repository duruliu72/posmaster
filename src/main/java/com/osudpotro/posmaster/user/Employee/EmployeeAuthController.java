package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserNotFoundException;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.auth.JwtResponse;
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
@RequestMapping("/employees/auth")
public class EmployeeAuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody EmployeeLoginRequest request, HttpServletResponse response) {
        User user=null;
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user= userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(
                    "User not found with email: " + request.getEmail()));;
        }
        if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
            // Search by EMAIL only
            user = userRepository.findByMobile(request.getMobile())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User not found with email: " + request.getMobile()));
        }
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
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
}
