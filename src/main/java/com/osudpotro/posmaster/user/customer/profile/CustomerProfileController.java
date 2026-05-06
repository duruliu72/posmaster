package com.osudpotro.posmaster.user.customer.profile;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.user.customer.*;
import com.osudpotro.posmaster.user.loginrecords.LoginRecord;
import com.osudpotro.posmaster.user.loginrecords.LoginRecordRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
@RestController
@RequestMapping("/web/customers/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final UserRepository userRepository;
    private final LoginRecordRepository loginRecordRepository;
    private final MultimediaRepository multimediaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;  // ← ADD THIS
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        try {
            // Get current authenticated user from AuthService
            User user = authService.getCurrentUser();
            Long userId = user.getId();

            log.info(" Building complete profile for user ID: {}, Email: {}", userId, user.getEmail());

            // FIX: Get ALL active sessions and handle multiple
            List<LoginRecord> activeSessions = loginRecordRepository
                    .findAllActiveSessionsByUserId(userId);

            CustomerLoginDeviceResponse currentDevice = null;

            if (!activeSessions.isEmpty()) {
                // Take the most recent session
                LoginRecord latestActive = activeSessions.get(0);
                currentDevice = mapToLoginDeviceResponse(latestActive);

                // Log warning if multiple sessions exist
                if (activeSessions.size() > 1) {
                    log.warn("⚠️ User {} has {} active sessions. Using most recent.",
                            userId, activeSessions.size());
                }
            }
            // 2. Get recent login history (last 5 logins)
            Page<LoginRecord> recentLogins = loginRecordRepository
                    .findByUserOrderByLoginTimeDesc(
                            user,
                            PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "loginTime"))
                    );

            List<CustomerLoginDeviceResponse> loginHistory = recentLogins.getContent()
                    .stream()
                    .map(this::mapToLoginDeviceResponse)
                    .collect(Collectors.toList());

            // 3. Get total login count
            Page<LoginRecord> allLogins = loginRecordRepository
                    .findByUserOrderByLoginTimeDesc(user, PageRequest.of(0, 1));
            Long totalLoginCount = allLogins.getTotalElements();

            // Convert Multimedia entity to MultimediaDto to avoid Hibernate proxy
            MultimediaDto profilePicDto = null;
            if (user.getProfilePic() != null) {
                Multimedia pic = user.getProfilePic();
                profilePicDto = new MultimediaDto();
                profilePicDto.setId(pic.getId());
                profilePicDto.setName(pic.getName());
                profilePicDto.setImageUrl(pic.getImageUrl());
                profilePicDto.setMediaType(pic.getMediaType());
                profilePicDto.setSourceLink(pic.getSourceLink());
            }

            // Build complete response
            CustomerProfileResponse response = CustomerProfileResponse.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .secondaryEmail(user.getSecondaryEmail())
                    .secondaryMobile(user.getSecondaryMobile())
                    .gender(user.getGender())
                    .profilePic(profilePicDto)  // Using DTO instead of entity
                    .provider(user.getProvider())
                    .providerId(user.getProviderId())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .currentActiveSession(currentDevice)
                    .recentLoginHistory(loginHistory)
                    .totalLoginCount(totalLoginCount)
                    .build();

            log.info(" Complete profile built - User: {}, Active: {}, History: {}, Total: {}",
                    userId,
                    currentDevice != null ? "Yes" : "No",
                    loginHistory.size(),
                    totalLoginCount
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error(" Error fetching profile: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Unauthorized",
                    "message", e.getMessage()
            ));
        }
    }

    // Helper method to map LoginRecord to LoginDeviceResponse
    private CustomerLoginDeviceResponse mapToLoginDeviceResponse(LoginRecord record) {
        return CustomerLoginDeviceResponse.builder()
                .id(record.getId())
                .deviceType(record.getDeviceType())
                .deviceBrand(record.getDeviceBrand())
                .deviceModel(record.getDeviceModel())
                .osName(record.getOsName())
                .browserName(record.getBrowserName())
                .location(record.getLocation())
                .country(record.getCountry())
                .city(record.getCity())
                .ipAddress(record.getIpAddress())
                .loginTime(record.getLoginTime())
                .logoutTime(record.getLogoutTime())
                .isActive(record.getIsActive())
                .sessionDurationSeconds(record.getSessionDurationSeconds())
                .loginMethod(record.getLoginMethod())
                .build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody CustomerUpdateRequest request) {
        try {
            User currentUser = authService.getCurrentUser();
            Customer customer = customerRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            CustomerDto updatedCustomer = customerService.updateMyProfile(request);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Profile updated successfully",
                    "data", updatedCustomer
            ));

        } catch (DuplicateCustomerException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            // Validate passwords
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "New password and confirm password do not match"
                ));
            }

            User user = authService.getCurrentUser();

            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Current password is incorrect"
                ));
            }

            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Password changed successfully"
            ));

        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload-profile-pic")
    public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only image files allowed"));
            }

            // Get current user
            User currentUser = authService.getCurrentUser();
            Customer customer = customerRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Create upload directory
            Path uploadPath = Paths.get("public/uploads/profiles/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file with unique name
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // Create Multimedia record
            Multimedia multimedia = new Multimedia();
            multimedia.setName(originalFilename);
            multimedia.setImageUrl("/uploads/profiles/" + filename);
            multimedia.setMediaType(1);
            multimedia.setSourceLink(1);
            multimedia.setLinked(true);

            Multimedia saved = multimediaRepository.save(multimedia);

            // Update user profile
            User user = customer.getUser();

            // Optional: Delete old profile pic if exists
            if (user.getProfilePic() != null) {
                // You can delete old file here if needed
            }

            user.setProfilePic(saved);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Profile picture updated successfully",
                    "imageUrl", saved.getImageUrl(),
                    "multimediaId", saved.getId()
            ));

        } catch (IOException e) {
            log.error("File upload error: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Failed to upload file"));
        }
    }


    @GetMapping("/logindevices")
    public ResponseEntity<?> getLoginDevices(
            @PageableDefault(size = 20, sort = "loginTime", direction = Sort.Direction.DESC) Pageable pageable) {

        try {
            User user = authService.getCurrentUser();
            Page<LoginRecord> devices = loginRecordRepository.findByUserOrderByLoginTimeDesc(user, pageable);
            return ResponseEntity.ok(devices);

        } catch (Exception e) {
            log.error("Error fetching login devices: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/activeSessions")
    public ResponseEntity<?> getActiveSessions() {
        try {
            User user = authService.getCurrentUser();

            Page<LoginRecord> activeSessions = loginRecordRepository
                    .findByUserOrderByLoginTimeDesc(user, Pageable.unpaged());

            Map<String, Object> response = new HashMap<>();
            response.put("totalActiveSessions", activeSessions.getTotalElements());
            response.put("sessions", activeSessions.map(record ->
                    Map.of(
                            "deviceType", record.getDeviceType(),
                            "deviceBrand", record.getDeviceBrand(),
                            "deviceModel", record.getDeviceModel(),
                            "location", record.getLocation(),
                            "loginTime", record.getLoginTime().toString()
                    )
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching active sessions: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}