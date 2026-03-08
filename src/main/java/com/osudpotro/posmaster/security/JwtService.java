//package com.osudpotro.posmaster.security;
//import com.osudpotro.posmaster.user.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import java.util.Date;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@AllArgsConstructor
//@Service
//public class JwtService {
//    private JwtConfig jwtConfig;
//
//    // Thread-safe token blacklist
//    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
//
//    public Jwt generateAccessToken(User user) {
//        return generateToken(user, jwtConfig.getAccessTokenExpiration());
//    }
//
//    public Jwt generateRefreshToken(User user) {
//        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
//    }
//
//    private Jwt generateToken(User user, long tokenExpiration) {
//        var expTimeMillis = System.currentTimeMillis() + 1000 * tokenExpiration;
//        var expIn = new Date(expTimeMillis);
//        Claims claims = Jwts.claims()
//                .subject(user.getId().toString())
//                .add("userName", user.getUserName())
//                .add("email", user.getEmail())
//                .add("mobile", user.getMobile())
//                .issuedAt(new Date())
//                .expiration(expIn)
//                .build();
//        return new Jwt(claims, jwtConfig.getSecretKey());
//    }
//
//    public Jwt parseToken(String token) {
//        try {
//            var claims = getClaims(token);
//            return new Jwt(claims, jwtConfig.getSecretKey());
//        } catch (JwtException e) {
//            return null;
//        }
//    }
//
//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(jwtConfig.getSecretKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    public String getUserEmailFromToken(String token) {
//        try {
//            var claims = getClaims(token);
//            return claims.get("email").toString();
//        } catch (JwtException e) {
//            return null;
//        }
//    }
//
//
//
//    public String getUserMobileFromToken(String token) {
//        try {
//            var claims = getClaims(token);
//            return claims.get("mobile").toString();
//        } catch (JwtException e) {
//            return null;
//        }
//    }
//
//
//
//    public String getUserIdFromToken(String token) {
//        try {
//            var claims = getClaims(token);
//            return claims.getSubject();
//        } catch (JwtException e) {
//            return null;
//        }
//    }
//
//    // LOGOUT METHODS
//    public void blacklistToken(String token) {
//        tokenBlacklist.add(token);
//        log.info(" Token blacklisted - Size: {}", tokenBlacklist.size());
//    }
//
//    public boolean isTokenBlacklisted(String token) {
//        return tokenBlacklist.contains(token);
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            if (isTokenBlacklisted(token)) {
//                log.info(" Token is blacklisted");
//                return false;
//            }
//            getClaims(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//
//    // Optional: Clean expired tokens from blacklist (call periodically)
//    public void cleanExpiredTokens() {
//        tokenBlacklist.removeIf(token -> {
//            try {
//                getClaims(token);
//                return false;
//            } catch (JwtException e) {
//                return true; // Remove if expired/invalid
//            }
//        });
//    }
//}
package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration()).toString();
    }

        public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenExpiration * 1000);

        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("mobile", user.getMobile())
                .add("userName", user.getUserName())
                .issuedAt(now)
                .expiration(expiry)
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }



    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserEmailFromToken(String token) {
        try {
            return getClaims(token).get("email", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserMobileFromToken(String token) {
        try {
            return getClaims(token).get("mobile", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}