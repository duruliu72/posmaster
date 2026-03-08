package com.osudpotro.posmaster.utility;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class DeviceDetectionUtil {

    private Parser uaParser;

    @PostConstruct
    public void init() {
        this.uaParser = new Parser();
        log.info("User-Agent Parser initialized");
    }

    public ClientInfo parseUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return ClientInfo.unknown();
        }

        try {
            Client client = uaParser.parse(userAgent);

            return ClientInfo.builder()
                    .deviceType(getDeviceType(client))
                    .deviceBrand(getDeviceBrand(client))
                    .osName(client.os.family != null ? client.os.family : "UNKNOWN")
                    .browserName(client.userAgent.family != null ? client.userAgent.family : "UNKNOWN")
                    .build();

        } catch (Exception e) {
            log.error("Error parsing user agent", e);
            return ClientInfo.unknown();
        }
    }

    private String getDeviceType(Client client) {
        if (client.device.family != null) {
            String device = client.device.family.toLowerCase();
            if (device.contains("phone") || device.contains("mobile")) return "MOBILE";
            if (device.contains("tablet") || device.contains("ipad")) return "TABLET";
            if (device.contains("tv")) return "TV";
        }
        return "DESKTOP";
    }

    private String getDeviceBrand(Client client) {
        if (client.device.family != null) {
            String device = client.device.family.toLowerCase();
            if (device.contains("iphone") || device.contains("ipad")) return "APPLE";
            if (device.contains("samsung")) return "SAMSUNG";
            if (device.contains("xiaomi") || device.contains("redmi")) return "XIAOMI";
            if (device.contains("huawei")) return "HUAWEI";
            if (device.contains("google") || device.contains("pixel")) return "GOOGLE";
            if (device.contains("oneplus")) return "ONEPLUS";
        }
        return "UNKNOWN";
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
    public static class ClientInfo {
        private String deviceType;
        private String deviceBrand;
        private String osName;
        private String browserName;

        public static ClientInfo unknown() {
            return ClientInfo.builder()
                    .deviceType("UNKNOWN")
                    .deviceBrand("UNKNOWN")
                    .osName("UNKNOWN")
                    .browserName("UNKNOWN")
                    .build();
        }
    }
}