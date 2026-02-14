package com.osudpotro.posmaster.auth;


import com.osudpotro.posmaster.security.CustomUserDetails;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.user.*;
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
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private  final CustomUserMapper userMapper;
    private final  AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = null;
        AdminUser adminUser = adminUserRepository.findByEmail(request.getEmail()).orElse(null);
        if(adminUser!=null){
            userId=adminUser.getId();
        }
        User user = userRepository.findUserWithAllPermissions(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
        var jwt=jwtService.parseToken(refreshToken);
        if (jwt==null||jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }
    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader){
        var token = authHeader.replace("Bearer ", "");
        var jwt=jwtService.parseToken(token);
        return jwt.isExpired();
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        User user =authService.getCurrentUser();
        var userDto=userMapper.toDto(user);
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