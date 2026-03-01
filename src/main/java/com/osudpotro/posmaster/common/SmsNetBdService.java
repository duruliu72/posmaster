package com.osudpotro.posmaster.common;

import com.osudpotro.posmaster.config.UtilityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNetBdService {

    private final UtilityConfig utilityConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSms(String to, String message) {
        try {
            String url = "https://api.sms.net.bd/sendsms";

            // Format: 017xxxxxxx -> 88017xxxxxxx
            String formattedNumber = formatPhoneNumber(to);

            log.info("📱 Sending SMS to: {} (formatted: {})", to, formattedNumber);

            // Create request body - WITHOUT sender_id
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("api_key", utilityConfig.getSmsNetbdApiKey());
            body.add("msg", message);
            body.add("to", formattedNumber);
            // ❌ REMOVED sender_id - this was causing error 413

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity =
                    new HttpEntity<>(body, headers);

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("SMS Response: {}", response.getBody());

            // Check if successful
            if (response.getBody() != null && response.getBody().contains("\"error\":0")) {
                log.info("✅ SMS sent successfully to: {}", to);
            } else {
                log.error("❌ SMS failed: {}", response.getBody());
            }

        } catch (Exception e) {
            log.error("❌ SMS sending failed: {}", e.getMessage());
        }
    }

    private String formatPhoneNumber(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.startsWith("0") && digits.length() == 11) {
            return "88" + digits.substring(1); // 017xxxxxxx -> 88017xxxxxxx
        }
        return digits;
    }
}