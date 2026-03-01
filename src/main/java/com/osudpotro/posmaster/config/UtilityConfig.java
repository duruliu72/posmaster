package com.osudpotro.posmaster.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.utility")
@Data
public class UtilityConfig {
    //private String twilioAccountSID;
    //  private String twilioAuthToken;
    // private String twilioPhoneNumber;
    private String googleClientId;
    private String googleClientSecret;
    private String emailUsername;
    private String emailPassword;
    private String smsNetbdApiKey;
    private String GoogleApiKey;
    // private String smsNetbdSenderId = "OSUDpotro";
}
