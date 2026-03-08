
//
//package com.osudpotro.posmaster.user.loginrecords;
//
//import com.osudpotro.posmaster.user.User;
//import com.osudpotro.posmaster.utility.DeviceDetectionUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class LoginRecordService {
//
//    private final LoginRecordRepository loginRecordRepository;
//    private final DeviceDetectionUtil deviceDetectionUtil;
//
//    @Transactional
//    public void recordLogin(User user, HttpServletRequest request, String loginMethod) {
//        try {
//            String userAgent = request.getHeader("User-Agent");
//            DeviceDetectionUtil.ClientInfo clientInfo = deviceDetectionUtil.parseUserAgent(userAgent);
//            String ipAddress = deviceDetectionUtil.getClientIp(request);
//
//            LoginRecord record = new LoginRecord();
//            record.setUser(user);
//            record.setUserEmail(user.getEmail());
//            record.setUserPhone(user.getMobile());
//            record.setUserName(user.getUserName());
//            record.setUserType(String.valueOf(user.getCustomer()));
//            record.setLoginTime(LocalDateTime.now());
//            record.setIsActive(true);
//            record.setAutoLoggedOut(false);
//            record.setIpAddress(ipAddress);
//            record.setUserAgent(userAgent);
//            record.setDeviceType(clientInfo.getDeviceType());
//            record.setDeviceBrand(clientInfo.getDeviceBrand());
//            record.setOsName(clientInfo.getOsName());
//            record.setBrowserName(clientInfo.getBrowserName());
//            record.setLoginMethod(loginMethod);
//            record.setLoginSuccess(true);
//            record.setStatus(1);
//
//            loginRecordRepository.save(record);
//            log.info(" LOGIN RECORDED - User: {}, Method: {}", user.getId(), loginMethod);
//
//        } catch (Exception e) {
//            log.error("Failed to record login", e);
//        }
//    }
//
//    @Transactional
//    public boolean recordLogout(User user) {
//        try {
//            Long userId = user.getId();
//            log.info(" Processing logout for user ID: {}", userId);
//
//            Optional<LoginRecord> activeSession = loginRecordRepository
//                    .findActiveSessionByUserId(userId);
//
//            if (activeSession.isPresent()) {
//                LoginRecord record = activeSession.get();
//                LocalDateTime now = LocalDateTime.now();
//                Long duration = ChronoUnit.SECONDS.between(record.getLoginTime(), now);
//
//                record.setLogoutTime(now);
//                record.setIsActive(false);
//                record.setSessionDurationSeconds(duration);
//
//                loginRecordRepository.save(record);
//                log.info(" LOGOUT SUCCESS - User: {}, Duration: {}s", userId, duration);
//                return true;
//            } else {
//                log.warn(" No active session found for user: {}", userId);
//                return false;
//            }
//
//        } catch (Exception e) {
//            log.error(" Logout error", e);
//            return false;
//        }
//    }
//}

package com.osudpotro.posmaster.user.loginrecords;

import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.utility.YauaaDeviceDetectionUtil;
import com.osudpotro.posmaster.utility.SimpleGeoIPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRecordService {

    private final LoginRecordRepository loginRecordRepository;
    private final YauaaDeviceDetectionUtil deviceDetectionUtil;
    private final SimpleGeoIPUtil geoIPUtil;

    @Transactional
    public void recordLogin(User user, HttpServletRequest request, String loginMethod) {
        try {
            String userAgent = request.getHeader("User-Agent");
            String ipAddress = deviceDetectionUtil.getClientIp(request);

            // Get device info from Yauaa
            YauaaDeviceDetectionUtil.DeviceInfo deviceInfo =
                    deviceDetectionUtil.parseUserAgent(userAgent);

            // Get location info from ip-api.com
            SimpleGeoIPUtil.LocationInfo locationInfo =
                    geoIPUtil.getLocation(ipAddress);

            LoginRecord record = new LoginRecord();
            record.setUser(user);
            record.setUserEmail(user.getEmail());
            record.setUserPhone(user.getMobile());
            record.setUserName(user.getUserName());
            record.setUserType(String.valueOf(user.getRoles()));
            record.setLoginTime(LocalDateTime.now());
            record.setIsActive(true);
            record.setAutoLoggedOut(false);
            record.setIpAddress(ipAddress);
            record.setUserAgent(userAgent);

            // Device info - REAL DATA from Yauaa
            record.setDeviceType(deviceInfo.getDeviceType());     // "Phone", "Tablet", "Desktop"
            record.setDeviceBrand(deviceInfo.getDeviceBrand());   // "Samsung", "Apple", "Google"
            record.setDeviceModel(deviceInfo.getDeviceModel());   // "SM-G991B", "iPhone 14 Pro"
            record.setOsName(deviceInfo.getOsName());             // "Android", "iOS", "Windows"
            record.setBrowserName(deviceInfo.getBrowserName());   // "Chrome", "Safari", "Firefox"

            // Location info - REAL DATA from ip-api.com
            record.setCountry(locationInfo.getCountry());         // "Bangladesh"
            record.setCity(locationInfo.getCity());               // "Dhaka"
            record.setLocation(locationInfo.getLocation());       // "Dhaka, Bangladesh"

            record.setLoginMethod(loginMethod);
            record.setLoginSuccess(true);
            record.setStatus(1);

            loginRecordRepository.save(record);

            log.info(" LOGIN RECORDED - User: {}, Device: {} {} ({}), OS: {}, Location: {}, IP: {}",
                    user.getId(),
                    deviceInfo.getDeviceBrand(),
                    deviceInfo.getDeviceModel(),
                    deviceInfo.getDeviceType(),
                    deviceInfo.getOsName(),
                    locationInfo.getLocation(),
                    ipAddress);

        } catch (Exception e) {
            log.error("Failed to record login for user: {}", user.getId(), e);
        }
    }

    @Transactional
    public boolean recordLogout(User user) {
        try {
            Long userId = user.getId();
            log.info("🔴 Logout for user ID: {}", userId);

            // This should find the active session
            Optional<LoginRecord> activeSession = loginRecordRepository
                    .findActiveSessionByUserId(userId);

            if (activeSession.isPresent()) {
                LoginRecord record = activeSession.get();
                LocalDateTime now = LocalDateTime.now();
                Long duration = ChronoUnit.SECONDS.between(record.getLoginTime(), now);

                record.setLogoutTime(now);
                record.setIsActive(false);
                record.setSessionDurationSeconds(duration);

                loginRecordRepository.save(record);
                log.info("✅ LOGOUT SUCCESS - User: {}, Duration: {}s", userId, duration);
                return true;
            } else {
                log.warn("⚠️ No active session for user: {}", userId);

                // Try backup method
                int updated = loginRecordRepository.logoutUser(userId, LocalDateTime.now(), 0L);
                if (updated > 0) {
                    log.info("✅ LOGOUT via backup - User: {}", userId);
                    return true;
                }
                return false;
            }

        } catch (Exception e) {
            log.error("❌ Logout error", e);
            return false;
        }

    }
}