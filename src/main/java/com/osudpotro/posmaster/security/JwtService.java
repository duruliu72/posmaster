package com.osudpotro.posmaster.security;

import com.osudpotro.posmaster.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        var expTimeMillis = System.currentTimeMillis() + 1000 * tokenExpiration;
        var expIn = new Date(expTimeMillis);
        String userName = "";
        String email = "";
        String mobile = "";
        if (user.getAdminUser() != null) {
            userName = user.getAdminUser().getUserName();
            email = user.getAdminUser().getEmail();
            mobile = user.getAdminUser().getMobile();
        }
        if (user.getEmployee() != null) {
            userName = user.getEmployee().getUserName();
            email = user.getEmployee().getEmail();
            mobile = user.getEmployee().getMobile();
        }
        if (user.getVehicleDriver() != null) {
            userName = user.getVehicleDriver().getUserName();
            email = user.getVehicleDriver().getEmail();
            mobile = user.getVehicleDriver().getMobile();
        }
        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("userName", userName)
                .add("email", email)
//                .add("mobile", mobile)
//                .add("role", user.getRoles())
                .issuedAt(new Date())
                .expiration(expIn)
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
            var claims = getClaims(token);
            return claims.get("email").toString();
        } catch (JwtException e) {
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
}