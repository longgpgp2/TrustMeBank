package com.trustme.config;

import com.trustme.config.filter.CookieToHeaderFilter;
import com.trustme.config.oauth2.CustomAuthenticationEntryPoint;
import com.trustme.config.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import com.trustme.config.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * String Configuration Component for configuring Spring Security & Oauth2, jwt functionalities
 */
@Configuration
public class SecurityConfig {
    // @Value("${jwt.signerKey}")

    private final CustomOAuth2AuthenticationSuccessHandler successHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CookieToHeaderFilter cookieToHeaderFilter;
    private final JwtDecoder jwtDecoder;

    public SecurityConfig(CustomOAuth2AuthenticationSuccessHandler successHandler, JwtAuthenticationFilter jwtAuthenticationFilter, CookieToHeaderFilter cookieToHeaderFilter, JwtDecoder jwtDecoder) {
        this.successHandler = successHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.cookieToHeaderFilter = cookieToHeaderFilter;
        this.jwtDecoder = jwtDecoder;
    }



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
                .addFilterBefore(cookieToHeaderFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer((oauth2) -> oauth2
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder))
                )
                .oauth2Login(oauth2-> oauth2
                        .successHandler(successHandler));

        // disable this to enable h2 functionalities within a frame
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }


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
}
