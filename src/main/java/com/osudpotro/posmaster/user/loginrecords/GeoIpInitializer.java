//package com.osudpotro.posmaster.user.loginrecords;
//
//import com.osudpotro.posmaster.user.loginrecords.LoginRecordService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GeoIpInitializer implements ApplicationRunner {
//
//    @Autowired
//    private LoginRecordService loginRecordService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // Path to your GeoIP database file
//        String databasePath = "src/main/resources/geolite2/GeoLite2-City.mmdb";
//        loginRecordService.initGeoIp(databasePath);
//    }
//}