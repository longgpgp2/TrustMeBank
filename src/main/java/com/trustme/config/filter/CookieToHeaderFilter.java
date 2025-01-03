package com.trustme.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Purposely set the secure HttpOnly cookie named 'jwt-token' to BearerToken for authorization.
 */
@Component
public class CookieToHeaderFilter extends OncePerRequestFilter {


    /**
     * Task: Try to simplify the filter.
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt-token".equals(cookie.getName())) {
                    request = new HttpServletRequestWrapper(request) {
                        @Override
                        public String getHeader(String name) {
                            if ("Authorization".equalsIgnoreCase(name)) {
                                return "Bearer " + cookie.getValue();
                            }
                            return super.getHeader(name);
                        }
                    };
                }
            }
        }
        chain.doFilter(request, response);
    }

}
