package com.trustme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class SecurityConfig {
//    @Value("${jwt.signerKey}")
     static String SIGNING_KEY_BASE64= "BynaRVN1R8shGkku6SmSnQJzGc8ZSs7aTQzDRlnD2ckNfZ+EDEInq0ap6Ktqcm6meg3sNQaLyDGOCRw6eMC1Vg==";

    /*
    *  1. The authentication Filter from Reading the Bearer Token passes a BearerTokenAuthenticationToken to the AuthenticationManager which is
    *  implemented by ProviderManager.
    *
    *  2. The ProviderManager is configured to use an AuthenticationProvider of type JwtAuthenticationProvider.
    *
    *  3. JwtAuthenticationProvider decodes, verifies, and validates the Jwt using a JwtDecoder.
    *
    *  4. JwtAuthenticationProvider then uses the JwtAuthenticationConverter to convert the Jwt into a Collection of granted authorities.
    *
    *  5. When authentication is successful, the Authentication that is returned is of type JwtAuthenticationToken and has a principal
    *  that is the Jwt returned by the configured JwtDecoder. Ultimately, the returned JwtAuthenticationToken will be set on the SecurityContextHolder
    *  by the authentication Filter.
    * */


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests->requests
                        .requestMatchers("/").authenticated()
                        .requestMatchers(HttpMethod.GET,"/admin").hasAuthority("admin:read")
                        .requestMatchers(HttpMethod.GET,"/posts").authenticated()
                        .requestMatchers(HttpMethod.POST,"/posts").hasAuthority("posts:write")
                        .anyRequest().permitAll())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))

        );

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

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");


        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
