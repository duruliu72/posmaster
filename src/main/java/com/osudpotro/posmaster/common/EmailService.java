package com.osudpotro.posmaster.common;

import com.osudpotro.posmaster.config.UtilityConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class EmailService {

    private final JavaMailSender mailSender;
    private final UtilityConfig utilityConfig;

    public void sendOtpEmail(String to, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP for Login - OsuDpotro");
            message.setText(String.format(
                    "Your One-Time Password (OTP) is: %s\n\n" +
                            "This OTP is valid for 5 minutes.\n" +
                            "Do not share this OTP with anyone.\n\n" +
                            "Thank you,\n" +
                            "OsuDpotro Team", otpCode));

            mailSender.send(message);
            log.info("OTP email sent to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    public void sendHtmlOtpEmail(String to, String otpCode) {
        try {
            // For HTML emails, you can use MimeMessage
            // This is a simple text version for now
            sendOtpEmail(to, otpCode);
        } catch (Exception e) {
            log.error("Failed to send HTML OTP email to: {}", to, e);
            throw new RuntimeException("Failed to send OTP email");
        }
    }
}