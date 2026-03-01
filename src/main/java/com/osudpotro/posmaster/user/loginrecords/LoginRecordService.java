//package com.osudpotro.posmaster.user.loginrecords;
//
//import com.maxmind.geoip2.DatabaseReader;
//import com.maxmind.geoip2.model.CityResponse;
//import com.maxmind.geoip2.record.City;
//import com.maxmind.geoip2.record.Country;
//import com.maxmind.geoip2.record.Location;
//import com.osudpotro.posmaster.user.User;
//import com.osudpotro.posmaster.user.loginrecords.LoginRecord;
//import com.osudpotro.posmaster.user.loginrecords.LoginRecordRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.net.InetAddress;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Optional;
//
//@Data
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class LoginRecordService {
//
//    private final LoginRecordRepository loginRecordRepository;
//    private DatabaseReader geoIpReader;
//
//    // Initialize GeoIP database
//    public void initGeoIp(String databasePath) {
//        try {
//            File database = new File(databasePath);
//            geoIpReader = new DatabaseReader.Builder(database).build();
//            log.info("GeoIP database loaded successfully");
//        } catch (Exception e) {
//            log.error("Failed to load GeoIP database: {}", e.getMessage());
//        }
//    }
//
//    @Transactional
//    public LoginRecord recordLogin(User user, HttpServletRequest request, String loginMethod) {
//        try {
//            LoginRecord record = new LoginRecord();
//            record.setUser(user);
//            record.setUserType(user.getUserType() != null ? user.getUserType().name() : "CUSTOMER");
//            record.setLoginTime(LocalDateTime.now());
//            record.setIsActive(true);
//            record.setLoginMethod(loginMethod);
//            record.setStatus(1);
//
//            // Get IP Address
//            String ipAddress = getClientIp(request);
//            record.setIpAddress(ipAddress);
//
//            // Get location from IP
//            if (geoIpReader != null && !isLocalIp(ipAddress)) {
//                try {
//                    InetAddress ip = InetAddress.getByName(ipAddress);
//                    CityResponse response = geoIpReader.city(ip);
//
//                    Country country = response.getCountry();
//                    City city = response.getCity();
//                    Location location = response.getLocation();
//
//                    record.setCountry(country.getName());
//                    record.setCity(city.getName());
//                    record.setRegion(response.getMostSpecificSubdivision() != null ?
//                            response.getMostSpecificSubdivision().getName() : null);
//
//                    if (location != null) {
//                        record.setLatitude(location.getLatitude());
//                        record.setLongitude(location.getLongitude());
//                    }
//
//                    // Get ISP info (requires separate API)
//                    // record.setIsp(getIspFromIp(ipAddress));
//
//                } catch (Exception e) {
//                    log.warn("Could not determine location for IP: {}", ipAddress);
//                }
//            }
//
//            // Parse User-Agent for device info
//            parseUserAgent(request.getHeader("User-Agent"), record);
//
//            // Get additional headers
//            record.setLanguage(request.getHeader("Accept-Language"));
//            record.setTimezone(request.getHeader("Timezone"));
//
//            // Screen resolution (sent from frontend)
//            String screenResolution = request.getHeader("Screen-Resolution");
//            if (screenResolution != null) {
//                record.setScreenResolution(screenResolution);
//            }
//
//            LoginRecord saved = loginRecordRepository.save(record);
//            log.info("Login recorded for user: {}, IP: {}, Device: {}",
//                    user.getEmail(), ipAddress, record.getDeviceType());
//
//            return saved;
//
//        } catch (Exception e) {
//            log.error("Error recording login: {}", e.getMessage());
//            return null;
//        }
//    }
//
//    @Transactional
//    public void recordLogout(User user) {
//        try {
//            Optional<LoginRecord> activeSession = loginRecordRepository
//                    .findTopByUserAndIsActiveTrueOrderByLoginTimeDesc(user);
//
//            if (activeSession.isPresent()) {
//                LoginRecord record = activeSession.get();
//                record.setLogoutTime(LocalDateTime.now());
//                record.setIsActive(false);
//
//                // Calculate session duration
//                long duration = ChronoUnit.SECONDS.between(
//                        record.getLoginTime(),
//                        record.getLogoutTime()
//                );
//                record.setSessionDuration(duration);
//
//                loginRecordRepository.save(record);
//                log.info("Logout recorded for user: {}, duration: {} seconds",
//                        user.getEmail(), duration);
//            } else {
//                log.warn("No active session found for user: {}", user.getEmail());
//            }
//        } catch (Exception e) {
//            log.error("Error recording logout: {}", e.getMessage());
//        }
//    }
//
//    private String getClientIp(HttpServletRequest request) {
//        String[] headers = {
//                "X-Forwarded-For",
//                "Proxy-Client-IP",
//                "WL-Proxy-Client-IP",
//                "HTTP_CLIENT_IP",
//                "HTTP_X_FORWARDED_FOR"
//        };
//
//        for (String header : headers) {
//            String ip = request.getHeader(header);
//            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
//                return ip.split(",")[0].trim();
//            }
//        }
//        return request.getRemoteAddr();
//    }
//
//    private boolean isLocalIp(String ip) {
//        return ip.equals("127.0.0.1") ||
//                ip.equals("0:0:0:0:0:0:0:1") ||
//                ip.startsWith("192.168.") ||
//                ip.startsWith("10.") ||
//                ip.startsWith("172.16.");
//    }
//
//    private void parseUserAgent(String userAgent, LoginRecord record) {
//        if (userAgent == null || userAgent.isEmpty()) {
//            record.setDeviceType("Unknown");
//            return;
//        }
//
//        // Device Type
//        if (userAgent.contains("Mobile")) {
//            record.setDeviceType("Mobile");
//        } else if (userAgent.contains("Tablet")) {
//            record.setDeviceType("Tablet");
//        } else {
//            record.setDeviceType("Desktop");
//        }
//
//        // OS Detection
//        if (userAgent.contains("Windows")) {
//            record.setOsName("Windows");
//            if (userAgent.contains("Windows NT 10.0")) record.setOsVersion("10");
//            else if (userAgent.contains("Windows NT 6.3")) record.setOsVersion("8.1");
//            else if (userAgent.contains("Windows NT 6.2")) record.setOsVersion("8");
//            else if (userAgent.contains("Windows NT 6.1")) record.setOsVersion("7");
//        } else if (userAgent.contains("Mac OS X")) {
//            record.setOsName("macOS");
//            String osVersion = extractBetween(userAgent, "Mac OS X ", ")");
//            record.setOsVersion(osVersion);
//        } else if (userAgent.contains("Android")) {
//            record.setOsName("Android");
//            String osVersion = extractBetween(userAgent, "Android ", ";");
//            record.setOsVersion(osVersion);
//        } else if (userAgent.contains("iOS") || userAgent.contains("iPhone")) {
//            record.setOsName("iOS");
//            String osVersion = extractBetween(userAgent, "OS ", " like");
//            record.setOsVersion(osVersion);
//        } else if (userAgent.contains("Linux")) {
//            record.setOsName("Linux");
//        }
//
//        // Browser Detection
//        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
//            record.setBrowserName("Chrome");
//            String version = extractBetween(userAgent, "Chrome/", " ");
//            record.setBrowserVersion(version);
//        } else if (userAgent.contains("Firefox")) {
//            record.setBrowserName("Firefox");
//            String version = extractBetween(userAgent, "Firefox/", " ");
//            record.setBrowserVersion(version);
//        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
//            record.setBrowserName("Safari");
//            String version = extractBetween(userAgent, "Version/", " ");
//            record.setBrowserVersion(version);
//        } else if (userAgent.contains("Edg")) {
//            record.setBrowserName("Edge");
//            String version = extractBetween(userAgent, "Edg/", " ");
//            record.setBrowserVersion(version);
//        }
//
//        // Device Brand/Model
//        if (userAgent.contains("iPhone")) {
//            record.setDeviceBrand("Apple");
//            record.setDeviceModel("iPhone");
//        } else if (userAgent.contains("iPad")) {
//            record.setDeviceBrand("Apple");
//            record.setDeviceModel("iPad");
//        } else if (userAgent.contains("Samsung")) {
//            record.setDeviceBrand("Samsung");
//        } else if (userAgent.contains("Xiaomi")) {
//            record.setDeviceBrand("Xiaomi");
//        } else if (userAgent.contains("OPPO")) {
//            record.setDeviceBrand("OPPO");
//        } else if (userAgent.contains("Vivo")) {
//            record.setDeviceBrand("Vivo");
//        } else if (userAgent.contains("Huawei")) {
//            record.setDeviceBrand("Huawei");
//        }
//
//        record.setUserAgent(userAgent);
//    }
//
//    private String extractBetween(String text, String start, String end) {
//        try {
//            int startIndex = text.indexOf(start);
//            if (startIndex == -1) return null;
//            startIndex += start.length();
//
//            int endIndex = end != null ? text.indexOf(end, startIndex) : text.length();
//            if (endIndex == -1) endIndex = text.length();
//
//            return text.substring(startIndex, endIndex).trim();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    // Analytics methods
//    public Long getActiveUserCount() {
//        return loginRecordRepository.countOnlineUsers();
//    }
//
//    public List<LoginRecord> getUserHistory(Long userId) {
//        return loginRecordRepository.findByUserId(userId, Pageable.unpaged()).getContent();
//    }
//
//    public List<LoginRecord> getActiveSessions() {
//        return loginRecordRepository.findByIsActiveTrue();
//    }
//}

package com.osudpotro.posmaster.user.loginrecords;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import com.osudpotro.posmaster.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;
import java.lang.String;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRecordService {

    private final LoginRecordRepository loginRecordRepository;
    private DatabaseReader geoIpReader;
    private static final String UNKNOWN = "Unknown";
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    );

    // Initialize GeoIP database - call this on startup
    public void initGeoIp(String databasePath) {
        try {
            File database = new File(databasePath);
            if (database.exists()) {
                geoIpReader = new DatabaseReader.Builder(database).build();
                log.info("✅ GeoIP database loaded successfully from: {}", databasePath);
            } else {
                log.warn("⚠️ GeoIP database not found at: {}. Location data will be unavailable.", databasePath);
            }
        } catch (Exception e) {
            log.error("❌ Failed to load GeoIP database: {}", e.getMessage());
        }
    }

    @Transactional
    public void recordLogin(User user, HttpServletRequest request, String loginMethod) {
        try {
            LoginRecord record = new LoginRecord();
            record.setUser(user);
            record.setUserType(user.getUserType() != null ? user.getUserType().name() : "CUSTOMER");
            record.setLoginTime(LocalDateTime.now());
            record.setIsActive(true);
            record.setLoginMethod(loginMethod);
            record.setStatus(1);

            // 📍 Get REAL IP Address
            String ipAddress = getClientIp(request);
            record.setIpAddress(ipAddress);
            log.info("📍 Client IP: {}", ipAddress);

            // 🌍 Get REAL location from IP (if not local)
            if (!isLocalIp(ipAddress) && geoIpReader != null) {
                try {
                    InetAddress inetAddress = InetAddress.getByName(ipAddress);
                    CityResponse response = geoIpReader.city(inetAddress);

                    // Country info
                    Country country = response.getCountry();
                    record.setCountry(country.getName());
                    record.setCountryCode(country.getIsoCode());

                    // City info
                    City city = response.getCity();
                    record.setCity(city.getName());

                    // Region/State info
                    Subdivision subdivision = response.getMostSpecificSubdivision();
                    if (subdivision != null) {
                        record.setRegion(subdivision.getName());
                        record.setRegionCode(subdivision.getIsoCode());
                    }

                    // Postal code
                    Postal postal = response.getPostal();
                    if (postal != null) {
                        record.setPostalCode(postal.getCode());
                    }

                    // Exact coordinates
                    Location location = response.getLocation();
                    if (location != null) {
                        record.setLatitude(location.getLatitude());
                        record.setLongitude(location.getLongitude());
                        record.setTimeZone(location.getTimeZone());
                        record.setAccuracyRadius(location.getAccuracyRadius());
                    }

                    // ISP info (requires separate API - MaxMind ISP database)
                    // record.setIsp(response.getTraits().getIsp());
                    // record.setOrganization(response.getTraits().getOrganization());

                    log.info("🌍 Location: {}, {}, {} ({} {})",
                            record.getCity(), record.getRegion(), record.getCountry(),
                            record.getLatitude(), record.getLongitude());

                } catch (Exception e) {
                    log.warn("⚠️ Could not determine location for IP {}: {}", ipAddress, e.getMessage());
                }
            }

            // 📱 Parse REAL device info from User-Agent
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && !userAgent.isEmpty()) {
                parseUserAgent(userAgent, record);
                record.setUserAgent(userAgent);
            }

            // 🌐 Get REAL network info from headers
            record.setAcceptLanguage(request.getHeader("Accept-Language"));
            record.setAcceptEncoding(request.getHeader("Accept-Encoding"));

            // 📡 Connection info
            String connection = request.getHeader("Connection");
            if (connection != null) {
                record.setConnectionType(connection);
            }

            // 📱 Screen resolution (sent from frontend)
            String screenResolution = request.getHeader("Screen-Resolution");
            if (screenResolution != null) {
                record.setScreenResolution(screenResolution);
            }

            // 🕒 Timezone from frontend (more accurate)
            String timezone = request.getHeader("Timezone");
            if (timezone != null) {
                record.setClientTimezone(timezone);
            }

            // 🔌 REAL request details
            record.setProtocol(request.getProtocol());
            record.setMethod(request.getMethod());
            record.setServerName(request.getServerName());
            record.setServerPort(request.getServerPort());
            record.setRequestUri(request.getRequestURI());
            record.setQueryString(request.getQueryString());

            LoginRecord saved = loginRecordRepository.save(record);
            log.info("✅ Login recorded for user: {}, IP: {}, Device: {}, Location: {}",
                    user.getEmail() != null ? user.getEmail() : user.getMobile(),
                    ipAddress, record.getDeviceType(), record.getCountry());

        } catch (Exception e) {
            log.error("❌ Error recording login: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void recordLogout(User user) {
        try {
            LoginRecord activeSession = loginRecordRepository
                    .findTopByUserAndIsActiveTrueOrderByLoginTimeDesc(user)
                    .orElse(null);

            if (activeSession != null) {
                activeSession.setLogoutTime(LocalDateTime.now());
                activeSession.setIsActive(false);

                // Calculate REAL session duration
                long duration = ChronoUnit.SECONDS.between(
                        activeSession.getLoginTime(),
                        activeSession.getLogoutTime()
                );
                activeSession.setSessionDuration(duration);

                loginRecordRepository.save(activeSession);
                log.info("✅ Logout recorded for user: {}, duration: {} seconds",
                        user.getEmail() != null ? user.getEmail() : user.getMobile(), duration);
            } else {
                log.warn("⚠️ No active session found for user: {}",
                        user.getEmail() != null ? user.getEmail() : user.getMobile());
            }
        } catch (Exception e) {
            log.error("❌ Error recording logout: {}", e.getMessage(), e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
                "X-Real-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For may contain multiple IPs, take the first one
                return ip.split(",")[0].trim();
            }
        }

        String remoteAddr = request.getRemoteAddr();
        // Handle IPv6 localhost
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return "127.0.0.1";
        }
        return remoteAddr;
    }

    private boolean isLocalIp(String ip) {
        return ip.equals("127.0.0.1") ||
                ip.equals("::1") ||
                ip.startsWith("192.168.") ||
                ip.startsWith("10.") ||
                ip.startsWith("172.16.") ||
                ip.startsWith("172.17.") ||
                ip.startsWith("172.18.") ||
                ip.startsWith("172.19.") ||
                ip.startsWith("172.20.") ||
                ip.startsWith("172.21.") ||
                ip.startsWith("172.22.") ||
                ip.startsWith("172.23.") ||
                ip.startsWith("172.24.") ||
                ip.startsWith("172.25.") ||
                ip.startsWith("172.26.") ||
                ip.startsWith("172.27.") ||
                ip.startsWith("172.28.") ||
                ip.startsWith("172.29.") ||
                ip.startsWith("172.30.") ||
                ip.startsWith("172.31.");
    }

    private void parseUserAgent(String userAgent, LoginRecord record) {
        if (userAgent == null || userAgent.isEmpty()) {
            record.setDeviceType(UNKNOWN);
            return;
        }

        // 📱 Device Type
        if (userAgent.contains("Mobile") || userAgent.contains("Android") && userAgent.contains("Mobile")) {
            record.setDeviceType("Mobile");
        } else if (userAgent.contains("Tablet") || userAgent.contains("iPad") ||
                userAgent.contains("Kindle") || userAgent.contains("Silk")) {
            record.setDeviceType("Tablet");
        } else {
            record.setDeviceType("Desktop");
        }

        // 💻 Operating System
        if (userAgent.contains("Windows NT")) {
            record.setOsName("Windows");
            if (userAgent.contains("Windows NT 10.0")) record.setOsVersion("10/11");
            else if (userAgent.contains("Windows NT 6.3")) record.setOsVersion("8.1");
            else if (userAgent.contains("Windows NT 6.2")) record.setOsVersion("8");
            else if (userAgent.contains("Windows NT 6.1")) record.setOsVersion("7");
            else if (userAgent.contains("Windows NT 6.0")) record.setOsVersion("Vista");
            else if (userAgent.contains("Windows NT 5.1")) record.setOsVersion("XP");
        } else if (userAgent.contains("Mac OS X")) {
            record.setOsName("macOS");
            record.setOsVersion(extractBetween(userAgent, "Mac OS X ", "[;)_]"));
        } else if (userAgent.contains("Android")) {
            record.setOsName("Android");
            record.setOsVersion(extractBetween(userAgent, "Android ", "[;)]"));
        } else if (userAgent.contains("iOS") || userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            record.setOsName("iOS");
            record.setOsVersion(extractBetween(userAgent, "OS ", " like Mac"));
        } else if (userAgent.contains("Linux")) {
            record.setOsName("Linux");
        }

        // 🌐 Browser
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg") && !userAgent.contains("OPR")) {
            record.setBrowserName("Chrome");
            record.setBrowserVersion(extractBetween(userAgent, "Chrome/", "[ .]"));
        } else if (userAgent.contains("Firefox") && !userAgent.contains("Seamonkey")) {
            record.setBrowserName("Firefox");
            record.setBrowserVersion(extractBetween(userAgent, "Firefox/", "[ .]"));
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome") && !userAgent.contains("CriOS")) {
            record.setBrowserName("Safari");
            record.setBrowserVersion(extractBetween(userAgent, "Version/", " "));
        } else if (userAgent.contains("Edg")) {
            record.setBrowserName("Edge");
            record.setBrowserVersion(extractBetween(userAgent, "Edg/", "[ .]"));
        } else if (userAgent.contains("OPR") || userAgent.contains("Opera")) {
            record.setBrowserName("Opera");
            record.setBrowserVersion(extractBetween(userAgent, "OPR/", "[ .]"));
        }

        // 📱 Device Brand/Model
        if (userAgent.contains("iPhone")) {
            record.setDeviceBrand("Apple");
            record.setDeviceModel("iPhone");
        } else if (userAgent.contains("iPad")) {
            record.setDeviceBrand("Apple");
            record.setDeviceModel("iPad");
        } else if (userAgent.contains("Macintosh")) {
            record.setDeviceBrand("Apple");
            record.setDeviceModel("Mac");
        } else if (userAgent.contains("Samsung") || userAgent.contains("SM-")) {
            record.setDeviceBrand("Samsung");
            record.setDeviceModel(extractModel(userAgent, new String[]{"SM-", "SAMSUNG-"}));
        } else if (userAgent.contains("Xiaomi") || userAgent.contains("Mi") || userAgent.contains("Redmi")) {
            record.setDeviceBrand("Xiaomi");
            record.setDeviceModel(extractModel(userAgent, new String[]{"Xiaomi ", "Mi ", "Redmi "}));
        } else if (userAgent.contains("OPPO") || userAgent.contains("CPH")) {
            record.setDeviceBrand("OPPO");
            record.setDeviceModel(extractModel(userAgent, new String[]{"OPPO ", "CPH"}));
        } else if (userAgent.contains("Vivo") || userAgent.contains("VIVO")) {
            record.setDeviceBrand("Vivo");
            record.setDeviceModel(extractModel(userAgent, new String[]{"Vivo ", "VIVO "}));
        } else if (userAgent.contains("Huawei") || userAgent.contains("Honor")) {
            record.setDeviceBrand("Huawei");
            record.setDeviceModel(extractModel(userAgent, new String[]{"Huawei ", "Honor "}));
        } else if (userAgent.contains("Google Pixel") || userAgent.contains("Pixel")) {
            record.setDeviceBrand("Google");
            record.setDeviceModel(extractModel(userAgent, new String[]{"Pixel "}));
        } else if (userAgent.contains("OnePlus")) {
            record.setDeviceBrand("OnePlus");
            record.setDeviceModel(extractModel(userAgent, new String[]{"OnePlus "}));
        } else if (userAgent.contains("Nokia")) {
            record.setDeviceBrand("Nokia");
            record.setDeviceModel(extractModel(userAgent, new String[]{"Nokia "}));
        }

        // 🌐 Browser Engine
        if (userAgent.contains("AppleWebKit")) {
            record.setBrowserEngine("WebKit");
        } else if (userAgent.contains("Gecko") && !userAgent.contains("WebKit")) {
            record.setBrowserEngine("Gecko");
        } else if (userAgent.contains("Blink")) {
            record.setBrowserEngine("Blink");
        } else if (userAgent.contains("Trident") || userAgent.contains("MSIE")) {
            record.setBrowserEngine("Trident");
        }
    }

    private String extractBetween(String text, String prefix, String delimiter) {
        try {
            int start = text.indexOf(prefix);
            if (start == -1) return null;
            start += prefix.length();

            int end = text.length();
            if (delimiter != null) {
                if (delimiter.startsWith("[")) {
                    // Handle regex-like delimiter (multiple possible delimiters)
                    String delims = delimiter.substring(1, delimiter.length() - 1);
                    for (char c : delims.toCharArray()) {
                        int pos = text.indexOf(c, start);
                        if (pos != -1 && pos < end) {
                            end = pos;
                        }
                    }
                } else {
                    int pos = text.indexOf(delimiter, start);
                    if (pos != -1) {
                        end = pos;
                    }
                }
            }

            return text.substring(start, end).trim();
        } catch (Exception e) {
            return null;
        }
    }

    private String extractModel(String userAgent, String[] prefixes) {
        for (String prefix : prefixes) {
            int index = userAgent.indexOf(prefix);
            if (index != -1) {
                int start = index + prefix.length();
                int end = userAgent.indexOf(' ', start);
                if (end == -1) end = userAgent.indexOf(';', start);
                if (end == -1) end = userAgent.indexOf(')', start);
                if (end == -1) end = userAgent.length();

                if (start < end) {
                    return userAgent.substring(start, end).trim();
                }
            }
        }
        return null;
    }

    // 📊 Analytics methods for production
    public Long getActiveUserCount() {
        return loginRecordRepository.countOnlineUsers();
    }

    public List<LoginRecord> getTodayLoginCount() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = start.plusDays(1);
        return loginRecordRepository.findByLoginTimeBetween(start, end);
    }

    public List<Object[]> getLoginStats(LocalDateTime start, LocalDateTime end) {
        return loginRecordRepository.getLoginStats(start, end);
    }
}