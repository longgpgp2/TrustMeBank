package com.trustme.config.oauth2;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Component for customizing Oauth2 unauthorized requests
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        System.out.println("Unauthorized request! Returning 401 status.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if(request.getRequestURI().equals("/auth/login")) {
            ResponseCookie cookieToRemove = ResponseCookie.from("jwt-token", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge( 1)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", cookieToRemove.toString());
            response.getWriter().write("Unauthorized session, please login again!");
        }

        else response.getWriter().write("Unauthorized access");
    }
}