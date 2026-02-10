package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.auth.JwtResponse;
import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.user.CustomUserMapper;
import com.osudpotro.posmaster.user.UserNotFoundException;
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
@RequestMapping("/admin/auth")
public class AdminAuthController {
    private final JwtService jwtService;
    private final AdminUserRepository adminUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final CustomUserMapper userMapper;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody AdminLoginRequest request, HttpServletResponse response) {
        String principal;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            principal = request.getEmail();
        } else if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            principal = request.getMobile();
        } else {
            throw new IllegalArgumentException("Either email or mobile must be provided");
        }
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal, request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
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
        if(adminUser==null){
            throw new UserNotFoundException("Email  is already registered");
        }
        var accessToken = jwtService.generateAccessToken(adminUser.getUser());
        var refreshToken = jwtService.generateRefreshToken(adminUser.getUser());
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }
}
