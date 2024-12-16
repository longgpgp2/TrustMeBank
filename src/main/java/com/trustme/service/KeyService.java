package com.trustme.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

/**
 * Service class for handling jwt encoding operations.
 * This class contains methods for generating signing keys and jwt.
 */
@Service
public class KeyService {

    String ENCODED_SIGNING_KEY_BASE64 = "BynaRVN1R8shGkku6SmSnQJzGc8ZSs7aTQzDRlnD2ckNfZ+EDEInq0ap6Ktqcm6meg3sNQaLyDGOCRw6eMC1Vg==";

    public String generateSigningKey() {
        byte[] key = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        String signingKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated Signing Key (Base64): " + signingKey);
        return signingKey;
    }
    /**
     * Generate a jwt based on the given username, scopes, and time.
     * @param username name of the authenticated user
     * @param scopes authorities according to user's roles
     * @param timeMillis life duration of the token
     * @return a jwt string that is then sent back to the client
     * */
    public String generateJwt(String username, List<String> scopes, Long timeMillis) {

        byte[] key = Base64.getDecoder().decode(ENCODED_SIGNING_KEY_BASE64);

        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuer("https://localhost:8080/auth")
                .setAudience("https://localhost:8080/api")
                .claim("scope", scopes)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeMillis * 1000))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwt;
    }
}
