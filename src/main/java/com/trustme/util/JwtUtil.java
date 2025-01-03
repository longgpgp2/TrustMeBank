package com.trustme.util;

import com.trustme.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    String ENCODED_SIGNING_KEY_BASE64 = "BynaRVN1R8shGkku6SmSnQJzGc8ZSs7aTQzDRlnD2ckNfZ+EDEInq0ap6Ktqcm6meg3sNQaLyDGOCRw6eMC1Vg==";

    /**
     * Extract authorities from user's role
     * @param user authenticated user
     * @return a list of authorities (scopes) for jwt
     * */
    public List<String> getJwtAuthoritiesFromRoles(UserDetails user) {
        List<Roles> roles = new ArrayList<>();
        user.getAuthorities().stream().forEach(role -> {
            roles.add(Roles.fromString(role.getAuthority()));
        });
        Set<String> authorities = new HashSet<>();
        roles.stream().forEach(role -> authorities.addAll(role.getAuthorities()));
        List<String> authoritiesList = authorities.stream().toList();
        return authoritiesList;
    }


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
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(ENCODED_SIGNING_KEY_BASE64).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, ENCODED_SIGNING_KEY_BASE64)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}