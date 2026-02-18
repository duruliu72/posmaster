package com.osudpotro.posmaster.common;

import com.osudpotro.posmaster.config.UtilityConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@AllArgsConstructor
@Service
public class SendSms {
    private UtilityConfig utilityConfig;

    public void sendSms(String recipientPhoneNumber, String sms) {
        Twilio.init(utilityConfig.getTwilioAccountSID(), utilityConfig.getTwilioAuthToken());
        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber), // To: your recipient's phone number
                        new PhoneNumber(utilityConfig.getTwilioPhoneNumber()), // From: your Twilio number
                        sms) // Message body
                .create();
        System.out.println("Message SID: " + message.getSid());
    }

    public void sendSms(String recipientPhoneNumber, String sms, String twilioPhoneNumber) {
        Twilio.init(utilityConfig.getTwilioAccountSID(), utilityConfig.getTwilioAuthToken());
        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber), // To: your recipient's phone number
                        new PhoneNumber(twilioPhoneNumber), // From: your Twilio number
                        sms) // Message body
                .create();
        System.out.println("Message SID: " + message.getSid());
    }
    public void sendEmailForOtpCode(String recipientPhoneNumber, String sms) {
        Twilio.init(utilityConfig.getTwilioAccountSID(), utilityConfig.getTwilioAuthToken());
        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber), // To: your recipient's phone number
                        new PhoneNumber(utilityConfig.getTwilioPhoneNumber()), // From: your Twilio number
                        sms) // Message body
                .create();
        System.out.println("Message SID: " + message.getSid());
    }
}
