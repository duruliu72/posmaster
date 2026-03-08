package com.osudpotro.posmaster.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class SimpleGeoIPUtil {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationInfo getLocation(String ipAddress) {
        // Skip local addresses
        if (ipAddress == null || ipAddress.isEmpty() ||
                ipAddress.startsWith("127.") ||
                ipAddress.startsWith("192.168.") ||
                ipAddress.startsWith("0:") ||
                ipAddress.equals("0:0:0:0:0:0:0:1") ||
                ipAddress.equals("localhost")) {

            return LocationInfo.unknown();
        }

        try {
            // Free ip-api.com - no API key required
            String url = "http://ip-api.com/json/" + ipAddress + "?fields=status,country,city,query";
            String response = restTemplate.getForObject(url, String.class);

            JsonNode json = objectMapper.readTree(response);

            if (json.has("status") && "success".equals(json.get("status").asText())) {
                String country = json.has("country") ? json.get("country").asText() : "Unknown";
                String city = json.has("city") ? json.get("city").asText() : "Unknown";
                String location = city + ", " + country;

                log.info("📍 Location - IP: {}, Country: {}, City: {}", ipAddress, country, city);

                return LocationInfo.builder()
                        .country(country)
                        .city(city)
                        .location(location)
                        .build();
            }

        } catch (Exception e) {
            log.debug("Could not get location for IP: {}", ipAddress);
        }

        return LocationInfo.unknown();
    }

    @Data
    @Builder
    public static class LocationInfo {
        private String country;
        private String city;
        private String location;

        public static LocationInfo unknown() {
            return LocationInfo.builder()
                    .country("Unknown")
                    .city("Unknown")
                    .location("Unknown")
                    .build();
        }
    }
}