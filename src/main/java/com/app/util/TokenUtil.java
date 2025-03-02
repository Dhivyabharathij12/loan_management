package com.app.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class TokenUtil {

    private static String getSecretKey(String userName){
        try {

            // Create an HMAC-SHA256 key
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(userName.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);

            // Generate the HMAC digest
            byte[] bytes = mac.doFinal(userName.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to a Base64 string
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }

    // Generate JWT Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, getSecretKey(username))
                .compact();
    }

    // Validate Token
    public static String validateToken(String userName, String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey(userName))
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
