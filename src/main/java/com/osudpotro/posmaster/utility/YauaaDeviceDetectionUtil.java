package com.osudpotro.posmaster.utility;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.PostConstruct;

@Slf4j
@Component
public class YauaaDeviceDetectionUtil {

    private UserAgentAnalyzer userAgentAnalyzer;

    @PostConstruct
    public void init() {
        this.userAgentAnalyzer = UserAgentAnalyzer.newBuilder()
                .withCache(10000)
                .hideMatcherLoadStats()
                .build();
        log.info("✅ Yauaa Device Detector initialized");
    }

    public DeviceInfo parseUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return DeviceInfo.unknown();
        }

        try {
            UserAgent agent = userAgentAnalyzer.parse(userAgent);

            // Extract all device information
            String deviceClass = agent.getValue("DeviceClass");        // Phone, Tablet, Desktop
            String deviceBrand = agent.getValue("DeviceBrand");        // Samsung, Apple, Google
            String deviceName = agent.getValue("DeviceName");          // SM-G991B, iPhone 14 Pro
            String osName = agent.getValue("OperatingSystemName");     // Android, iOS, Windows
            String browserName = agent.getValue("AgentName");          // Chrome, Safari, Firefox

            log.info("📱 Device detected - Class: {}, Brand: {}, Model: {}, OS: {}, Browser: {}",
                    deviceClass, deviceBrand, deviceName, osName, browserName);

            return DeviceInfo.builder()
                    .deviceType(deviceClass)      // Maps to your deviceType field
                    .deviceBrand(deviceBrand)      // Maps to your deviceBrand field
                    .deviceModel(deviceName)       // Maps to your deviceModel field
                    .osName(osName)                 // Maps to your osName field
                    .browserName(browserName)       // Maps to your browserName field
                    .build();

        } catch (Exception e) {
            log.error("Error parsing user agent: {}", e.getMessage());
            return DeviceInfo.unknown();
        }
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Data
    @Builder
    public static class DeviceInfo {
        private String deviceType;      // Phone, Tablet, Desktop
        private String deviceBrand;      // Samsung, Apple, Google
        private String deviceModel;      // SM-G991B, iPhone 14 Pro
        private String osName;           // Android, iOS, Windows
        private String browserName;       // Chrome, Safari, Firefox

        public static DeviceInfo unknown() {
            return DeviceInfo.builder()
                    .deviceType("Unknown")
                    .deviceBrand("Unknown")
                    .deviceModel("Unknown")
                    .osName("Unknown")
                    .browserName("Unknown")
                    .build();
        }
    }
}