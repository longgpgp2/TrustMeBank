package com.trustme.config;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import com.trustme.config.oauth2.CustomAuthenticationEntryPoint;
import com.trustme.config.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * String Configuration Component for configuring Spring Security & Oauth2, jwt functionalities
 */
@Configuration
public class SecurityConfig {
    // @Value("${jwt.signerKey}")
    static String SIGNING_KEY_BASE64 = "BynaRVN1R8shGkku6SmSnQJzGc8ZSs7aTQzDRlnD2ckNfZ+EDEInq0ap6Ktqcm6meg3sNQaLyDGOCRw6eMC1Vg==";
    @Autowired
    private CustomOAuth2AuthenticationSuccessHandler successHandler;
    /**
     * 1. The authentication Filter from Reading the Bearer Token passes a
     * BearerTokenAuthenticationToken to the AuthenticationManager which is
     * implemented by ProviderManager.
     *
     * 2. The ProviderManager is configured to use an AuthenticationProvider of type
     * JwtAuthenticationProvider.
     *
     * 3. JwtAuthenticationProvider decodes, verifies, and validates the Jwt using a
     * JwtDecoder.
     *
     * 4. JwtAuthenticationProvider then uses the JwtAuthenticationConverter to
     * convert the Jwt into a Collection of granted authorities.
     *
     * 5. When authentication is successful, the Authentication that is returned is
     * of type JwtAuthenticationToken and has a principal
     * that is the Jwt returned by the configured JwtDecoder. Ultimately, the
     * returned JwtAuthenticationToken will be set on the SecurityContextHolder
     * by the authentication Filter.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/admins").hasAuthority("admin:manage")
                        .requestMatchers("/admin/users").hasAuthority("user:manage")
                        .requestMatchers("/admin/transactions").hasAuthority("transaction:manage")
                        .requestMatchers("/admin/**").hasAuthority("admin:read")
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/auth/info").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                )
                .oauth2Login(oauth2-> oauth2
                        .successHandler(successHandler));

        // disable this to enable h2 functionalities within a frame
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] decodedKey = Base64.getDecoder().decode(SIGNING_KEY_BASE64);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "HS512");

        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    /**
     * Bean for configuring jwt claims' prefixes.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
