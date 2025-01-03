package com.trustme.config;
import com.trustme.config.filter.CookieToHeaderFilter;
import com.trustme.config.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Testing config class
 * Tried to register the order of the filters but failed.
 */
//@Configuration
public class FilterConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CookieToHeaderFilter cookieToHeaderFilter;

    public FilterConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CookieToHeaderFilter cookieToHeaderFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.cookieToHeaderFilter = cookieToHeaderFilter;
    }

    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/api/*", "/auth/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    public FilterRegistrationBean<CookieToHeaderFilter> cookieToHeaderFilter() {
        FilterRegistrationBean<CookieToHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cookieToHeaderFilter);
        registrationBean.addUrlPatterns("/api/*", "/auth/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
