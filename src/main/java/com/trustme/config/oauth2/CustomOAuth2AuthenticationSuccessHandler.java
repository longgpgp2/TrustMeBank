package com.trustme.config.oauth2;

import com.trustme.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@Configuration
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;

    public CustomOAuth2AuthenticationSuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Authentication successful, start sending cookies!");
        String token = authService.generateJwt(authentication.getName(), null, 3600L);

        Cookie jwtCookie = new Cookie("jwt-token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(request.isSecure());
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(jwtCookie);

        response.sendRedirect("/");
    }
}
