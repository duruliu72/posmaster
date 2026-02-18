package com.osudpotro.posmaster.web.customer;


import com.osudpotro.posmaster.common.SendSms;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.auth.JwtResponse;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/web/customers")
public class WebCustomerController {
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private final SendSms sendSms;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody WebCustomerLoginRequest request, HttpServletResponse response) {
        User user = null;
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() && request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
//            Spring Auth
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user == null) {
                User newUser = new User();
                newUser.setEmail(request.getEmail());
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("Customer");
                            role.setRoleKey("ROLE_CUSTOMER");
                            role.setUsers(new HashSet<>());
                            role.setPermissions(new HashSet<>());
                            return roleRepository.save(role);
                        });
                // ===SET ROLE ADMIN USER  ===
                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                customerRepository.save(newCustomer);
                user = newUser;
            }
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        }
        if (request.getMobile() != null && !request.getMobile().trim().isEmpty() && request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
//            Spring Auth
            user = userRepository.findByMobile(request.getMobile()).orElse(null);
            if (user == null) {
                User newUser = new User();
                newUser.setMobile(request.getMobile());
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("Customer");
                            role.setRoleKey("ROLE_CUSTOMER");
                            role.setUsers(new HashSet<>());
                            role.setPermissions(new HashSet<>());
                            return roleRepository.save(role);
                        });
                // ===SET ROLE ADMIN USER  ===
                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                customerRepository.save(newCustomer);
                user = newUser;
            }
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() && request.getOtpCode() != null && !request.getOtpCode().trim().isEmpty()) {
//            OTP Based
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user != null) {
                var customer = user.getCustomer();
                if ((customer.getOtpCode() == null && customer.getOtpRequestDateTime() == null) || (customer.getOtpCode() != null && !customer.getOtpCode().trim().isEmpty() && !customer.getOtpCode().equals(request.getOtpCode()))) {
                    throw new UnauthorizedException("Invalid Otp");
                }
                if (customer.getOtpRequestDateTime().isBefore(LocalDateTime.now())) {
                    throw new UnauthorizedException("Time Over");
                }
                customer.setOtpCode(null);
                customer.setOtpRequestDateTime(null);
                customerRepository.save(customer);
            }
        }
        if (request.getMobile() != null && !request.getMobile().trim().isEmpty() && request.getOtpCode() != null && !request.getOtpCode().trim().isEmpty()) {
//            Otp Based Mobile
            user = userRepository.findByMobile(request.getMobile()).orElse(null);
            if (user != null) {
                var customer = user.getCustomer();
                if ((customer.getOtpCode() == null && customer.getOtpRequestDateTime() == null) || (customer.getOtpCode() != null && !customer.getOtpCode().trim().isEmpty() && !customer.getOtpCode().equals(request.getOtpCode()))) {
                    throw new UnauthorizedException("Invalid Otp");
                }
                if (customer.getOtpRequestDateTime().isBefore(LocalDateTime.now())) {
                    throw new UnauthorizedException("Time Over");
                }
                customer.setOtpCode(null);
                customer.setOtpRequestDateTime(null);
                customerRepository.save(customer);
            }
        }
        if (request.getProvider() != null && !request.getProvider().trim().isEmpty() && request.getProviderId() != null && !request.getProviderId().trim().isEmpty()) {
//            Provider Auth
        }
//        if ((request.getEmail() != null && !request.getEmail().trim().isEmpty() || request.getMobile() != null && !request.getMobile().trim().isEmpty()) && request.getPassword() == null) {
//            throw new UnauthorizedException("Credential mismatch");
//        }
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

    @PostMapping("/send-otp")
    public ResponseEntity<OtpResponse> sendOtp(@Validated @RequestBody WebCustomerOtpRequest request, HttpServletResponse response) {
        User user = null;
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
//            OTP Auth By Email
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user == null) {
                User newUser = new User();
                newUser.setEmail(request.getEmail());
                Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("Customer");
                            role.setRoleKey("ROLE_CUSTOMER");
                            role.setUsers(new HashSet<>());
                            role.setPermissions(new HashSet<>());
                            return roleRepository.save(role);
                        });
                // ===SET ROLE ADMIN USER  ===
                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                customerRepository.save(newCustomer);
                user = newUser;
            }
        }
        if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
//            OTP Auth
            user = userRepository.findByMobile(request.getMobile()).orElse(null);
            if (user == null) {
                User newUser = new User();
                newUser.setMobile(request.getMobile());
                Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("Customer");
                            role.setRoleKey("ROLE_CUSTOMER");
                            role.setUsers(new HashSet<>());
                            role.setPermissions(new HashSet<>());
                            return roleRepository.save(role);
                        });
                // ===SET ROLE ADMIN USER  ===
                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                customerRepository.save(newCustomer);
                newUser.setCustomer(newCustomer);
                user = newUser;
            }
        }
        String otpCode = "";
        if (user != null) {
            otpCode = generateOtp();
            Customer customer = user.getCustomer();
            customer.setOtpCode(otpCode);
            customer.setOtpRequestDateTime(LocalDateTime.now().plusMinutes(5));
//            Send Sms
            String mobileSms="Your  OTP:"+otpCode;
//            sendSms.sendSms(request.getMobile(),mobileSms);
            customerRepository.save(customer);
        }
        return ResponseEntity.ok(new OtpResponse(otpCode));
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(999999));
    }

}
