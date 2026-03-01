package com.osudpotro.posmaster.web.customer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.osudpotro.posmaster.common.EmailService;
import com.osudpotro.posmaster.common.SmsNetBdService;
import com.osudpotro.posmaster.config.UtilityConfig;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.security.JwtConfig;
import com.osudpotro.posmaster.security.JwtService;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.auth.JwtResponse;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerCreateRequest;
import com.osudpotro.posmaster.user.customer.CustomerMapper;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import com.osudpotro.posmaster.user.loginrecords.LoginRecordService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
@Data
@Slf4j
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
    private final CustomerMapper customerMapper;
    private final MultimediaRepository  multimediaRepository;
    private final UtilityConfig utilityConfig;
    private final EmailService emailService;
    private final LoginRecordService loginRecordService;
    private final Random random = new Random();
    private final SmsNetBdService sendSms;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody WebCustomerLoginRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {
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
        // ==================== PROVIDER AUTH (GOOGLE) ====================
        if (request.getProvider() != null && !request.getProvider().trim().isEmpty() &&
                request.getProviderId() != null && !request.getProviderId().trim().isEmpty()) {

            if ("GOOGLE".equalsIgnoreCase(request.getProvider())) {
                try {
                    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(),
                            new GsonFactory()
                    ).setAudience(Collections.singletonList(utilityConfig.getGoogleClientId())).build();

                    GoogleIdToken idToken = verifier.verify(request.getProviderId());

                    if (idToken == null) {
                        throw new UnauthorizedException("Invalid Google token");
                    }

                    GoogleIdToken.Payload payload = idToken.getPayload();

                    // Extract Google user info
                    String email = payload.getEmail();                          // Email
                    String googleId = payload.getSubject();                     // User ID
                    String givenName = (String) payload.get("given_name");     // First Name
                    String familyName = (String) payload.get("family_name");   // Last Name
                    String displayName = (String) payload.get("name");         // Display Name (use as userName)
                    String picture = (String) payload.get("picture");          // Photo URL

                    log.info("Google login - email: {}, givenName: {}, familyName: {}, displayName: {}",
                            email, givenName, familyName, displayName);

                    // Check if user exists by email
                    user = userRepository.findByEmail(email).orElse(null);

                    if (user == null) {
                        // Create new user using your mapper
                        CustomerCreateRequest createRequest = new CustomerCreateRequest();
                        createRequest.setEmail(email);
                        createRequest.setUserName(displayName);        // Display Name → userName
                        createRequest.setFirstName(givenName);         // Given Name → firstName
                        createRequest.setLastName(familyName);         // Family Name → lastName
                        createRequest.setProvider("GOOGLE");
                        createRequest.setProviderId(googleId);         // User ID → providerId

                        String randomPassword = "GOOGLE_" + System.currentTimeMillis();
                        createRequest.setPassword(randomPassword);

                        user = customerMapper.toUserEntity(createRequest);
                        user.setPassword(passwordEncoder.encode(randomPassword));
                        user.setUserType(UserType.CUSTOMER);


                        //  Save profile picture URL
                        if (picture != null && !picture.isEmpty()) {
                            // Create a Multimedia record for the profile picture
                            Multimedia profilePic = new Multimedia();
                            profilePic.setImageUrl(picture);
                            profilePic.setName("google_profile_" + System.currentTimeMillis());
                            // Save multimedia (you need a repository for this)
                            multimediaRepository.save(profilePic);
                            user.setProfilePic(profilePic);
                        }



                        Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                                .orElseGet(() -> {
                                    Role role = new Role();
                                    role.setName("Customer");
                                    role.setRoleKey("ROLE_CUSTOMER");
                                    role.setUsers(new HashSet<>());
                                    role.setPermissions(new HashSet<>());
                                    return roleRepository.save(role);
                                });
                        user.setRoles(Set.of(findRole));
                        user = userRepository.save(user);

                        // Create customer (just link to user)
                        Customer newCustomer = new Customer();
                        newCustomer.setUser(user);
                        customerRepository.save(newCustomer);

                        log.info("New user created from Google: {}", email);

                    } else {
                        // Update existing user with Google info if not set
                        boolean updated = false;

                        if (user.getProviderId() == null) {
                            user.setProvider("GOOGLE");
                            user.setProviderId(googleId);
                            updated = true;
                        }

                        if (user.getFirstName() == null && givenName != null) {
                            user.setFirstName(givenName);
                            updated = true;
                        }

                        if (user.getLastName() == null && familyName != null) {
                            user.setLastName(familyName);
                            updated = true;
                        }

                        if (user.getUserName() == null && displayName != null) {
                            user.setUserName(displayName);
                            updated = true;
                        }


                        //  Update profile picture if not set
                        if (user.getProfilePic() == null && picture != null && !picture.isEmpty()) {
                            Multimedia profilePic = new Multimedia();
                            profilePic.setImageUrl(picture);
                            profilePic.setName("google_profile_" + System.currentTimeMillis());
                            multimediaRepository.save(profilePic);
                            user.setProfilePic(profilePic);
                            updated = true;
                        }
                        if (updated) {
                            userRepository.save(user);
                        }

                        log.info("Existing user updated with Google info: {}", email);
                    }

                } catch (Exception e) {
                    log.error("Google login error: {}", e.getMessage());
                    throw new UnauthorizedException("Google authentication failed");
                }
            }
        }
        //==================== PROVIDER AUTH (FACEBOOK) ====================
        if (request.getProvider() != null && !request.getProvider().trim().isEmpty() &&
                request.getProviderId() != null && !request.getProviderId().trim().isEmpty()) {

            //                FACEBOOK LOGIN
            if ("FACEBOOK".equalsIgnoreCase(request.getProvider())) {
                try {
                    String accessToken = request.getProviderId();

                    // Get user info with picture URL that returns viewable image
                    String url = "https://graph.facebook.com/v18.0/me?fields=id,name,email,first_name,last_name,picture.width(500).height(500)&access_token=" + accessToken;

                    RestTemplate restTemplate = new RestTemplate();
                    String facebookresponse = restTemplate.getForObject(url, String.class);

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode userNode = mapper.readTree(facebookresponse);

                    // Extract Facebook user info
                    String email = userNode.has("email") ? userNode.get("email").asText() : null;
                    String facebookId = userNode.get("id").asText();
                    String firstName = userNode.has("first_name") ? userNode.get("first_name").asText() : "";
                    String lastName = userNode.has("last_name") ? userNode.get("last_name").asText() : "";
                    String displayName = userNode.get("name").asText();

                    //  Get viewable profile picture URL
                    String picture = null;
                    if (userNode.has("picture") &&
                            userNode.get("picture").has("data") &&
                            userNode.get("picture").get("data").has("url")) {

                        // This URL will show image directly in browser
                        picture = userNode.get("picture").get("data").get("url").asText();

                        // Remove any download parameters if present
                        if (picture.contains("&as=site")) {
                            picture = picture.split("&as=site")[0];
                        }
                    }

                    log.info("Facebook login - email: {}, firstName: {}, lastName: {}, picture: {}",
                            email, firstName, lastName, picture);

                    // Check if user exists by email
                    User facebookUser = null;
                    if (email != null) {
                        facebookUser = userRepository.findByEmail(email).orElse(null);
                    }

                    if (facebookUser == null) {
                        facebookUser = userRepository.findByProviderId(facebookId).orElse(null);
                    }

                    if (facebookUser == null) {
                        // Create new user using your mapper
                        CustomerCreateRequest createRequest = new CustomerCreateRequest();
                        createRequest.setEmail(email);
                        createRequest.setUserName(displayName);
                        createRequest.setFirstName(firstName);
                        createRequest.setLastName(lastName);
                        createRequest.setProvider("FACEBOOK");
                        createRequest.setProviderId(facebookId);

                        String randomPassword = "FACEBOOK_" + System.currentTimeMillis();
                        createRequest.setPassword(randomPassword);

                        facebookUser = customerMapper.toUserEntity(createRequest);
                        facebookUser.setPassword(passwordEncoder.encode(randomPassword));
                        facebookUser.setUserType(UserType.CUSTOMER);

                        //  Save profile picture URL (SAME as Google!)
                        if (picture != null && !picture.isEmpty()) {
                            Multimedia profilePic = new Multimedia();
                            profilePic.setImageUrl(picture);  // Stores viewable URL
                            profilePic.setName("facebook_profile_" + System.currentTimeMillis());
                            multimediaRepository.save(profilePic);
                            facebookUser.setProfilePic(profilePic);
                        }

                        Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                                .orElseGet(() -> {
                                    Role role = new Role();
                                    role.setName("Customer");
                                    role.setRoleKey("ROLE_CUSTOMER");
                                    role.setUsers(new HashSet<>());
                                    role.setPermissions(new HashSet<>());
                                    return roleRepository.save(role);
                                });
                        facebookUser.setRoles(Set.of(findRole));
                        facebookUser = userRepository.save(facebookUser);

                        // Create customer
                        Customer newCustomer = new Customer();
                        newCustomer.setUser(facebookUser);
                        customerRepository.save(newCustomer);

                        log.info(" New CUSTOMER created from Facebook");

                    } else {
                        // Update existing user
                        boolean updated = false;

                        if (facebookUser.getProviderId() == null) {
                            facebookUser.setProvider("FACEBOOK");
                            facebookUser.setProviderId(facebookId);
                            updated = true;
                        }

                        if (facebookUser.getFirstName() == null && firstName != null && !firstName.isEmpty()) {
                            facebookUser.setFirstName(firstName);
                            updated = true;
                        }

                        if (facebookUser.getLastName() == null && lastName != null && !lastName.isEmpty()) {
                            facebookUser.setLastName(lastName);
                            updated = true;
                        }

                        if (facebookUser.getUserName() == null && displayName != null) {
                            facebookUser.setUserName(displayName);
                            updated = true;
                        }

                        //  Update profile picture if not set (SAME as Google!)
                        if (facebookUser.getProfilePic() == null && picture != null && !picture.isEmpty()) {
                            Multimedia profilePic = new Multimedia();
                            profilePic.setImageUrl(picture);
                            profilePic.setName("facebook_profile_" + System.currentTimeMillis());
                            multimediaRepository.save(profilePic);
                            facebookUser.setProfilePic(profilePic);
                            updated = true;
                        }

                        if (updated) {
                            userRepository.save(facebookUser);
                        }

                        log.info(" Existing CUSTOMER updated with Facebook info");
                    }

                    user = facebookUser;

                } catch (Exception e) {
                    log.error("Facebook login error: {}", e.getMessage());
                    throw new UnauthorizedException("Facebook authentication failed");
                }
            }

        }

        if (user != null) {
            // Determine login method
            String loginMethod = "UNKNOWN";
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                loginMethod = "PASSWORD";
            } else if (request.getOtpCode() != null && !request.getOtpCode().isEmpty()) {
                loginMethod = "OTP";
            } else if (request.getProvider() != null && !request.getProvider().isEmpty()) {
                loginMethod = request.getProvider(); // GOOGLE, FACEBOOK
            }

            // ✅ RECORD THE LOGIN
            loginRecordService.recordLogin(user, httpRequest, loginMethod);
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


    @PostMapping("/send-otp")
    public ResponseEntity<OtpResponse> sendOtp(@Validated @RequestBody WebCustomerOtpRequest request) {
        User user = null;

        // Handle Email OTP
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail()).orElse(null);

            if (user == null) {
                // Create new user
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

                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);

                //  IMPORTANT: Create and save Customer FIRST
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                newCustomer = customerRepository.save(newCustomer);  // Save to get ID

                //  Then set the customer back to user
                newUser.setCustomer(newCustomer);
                user = newUser;

                log.info("Created new user with customer: {}", newCustomer.getId());
            }
        }

        // Handle Mobile OTP
        if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
            user = userRepository.findByMobile(request.getMobile()).orElse(null);

            if (user == null) {
                // Create new user
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

                newUser.setRoles(Set.of(findRole));
                newUser = userRepository.save(newUser);

                //  IMPORTANT: Create and save Customer FIRST
                Customer newCustomer = new Customer();
                newCustomer.setUser(newUser);
                newCustomer = customerRepository.save(newCustomer);  // Save to get ID

                //  Then set the customer back to user
                newUser.setCustomer(newCustomer);
                user = newUser;

                log.info("Created new user with customer: {}", newCustomer.getId());
            }
        }

        String otpCode = "";
        if (user != null) {
            otpCode = generateOtp();

            //  Get customer - now this won't be null
            Customer customer = user.getCustomer();

            if (customer == null) {
                // Just in case, create customer if still null
                customer = new Customer();
                customer.setUser(user);
                customer = customerRepository.save(customer);
                user.setCustomer(customer);
                userRepository.save(user);
            }

            customer.setOtpCode(otpCode);
            customer.setOtpRequestDateTime(LocalDateTime.now().plusMinutes(5));
            customerRepository.save(customer);

            // Send SMS if mobile exists
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                String mobileSms =

                        //  " Your OTP for Login - OsuDpotro: " + otpCode;

                        "Your OTP for Login - " + otpCode +"\n"+
                                "This OTP is valid for 5 minutes.\n" +
                                "Thank you,\n"
                                + "OsuDpotro Team";

                sendSms.sendSms(request.getMobile(), mobileSms);
            }

            // Send Email if email exists
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                try {
                    emailService.sendOtpEmail(request.getEmail(), otpCode);
                    log.info("OTP email sent to: {}", request.getEmail());
                } catch (Exception e) {
                    log.error("Failed to send OTP email: {}", e.getMessage());
                }
            }
        }

        return ResponseEntity.ok(new OtpResponse(otpCode));
    }


    private String generateOtp() {
        return String.format("%06d", random.nextInt(999999));
    }


}
