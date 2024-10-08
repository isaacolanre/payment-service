package com.payment.service.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
//@EnableWebMvc
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        // CORS configuration for API endpoints
        CorsConfiguration apiCorsConfig = new CorsConfiguration();
        apiCorsConfig.setAllowCredentials(true);
        apiCorsConfig.addAllowedOriginPattern("*"); // Adjust as necessary
        apiCorsConfig.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT));
        apiCorsConfig.setAllowedMethods(Arrays.asList(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        apiCorsConfig.setMaxAge(36000L);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", apiCorsConfig);

        // Default CORS configuration for other endpoints, to ensure cors does not interfere with static resources
        CorsConfiguration defaultCorsConfig = new CorsConfiguration();
        defaultCorsConfig.setAllowCredentials(true);
        defaultCorsConfig.addAllowedOriginPattern("*");
        defaultCorsConfig.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT));
        defaultCorsConfig.setAllowedMethods(Arrays.asList(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        defaultCorsConfig.setMaxAge(36000L);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", defaultCorsConfig);

        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(urlBasedCorsConfigurationSource));
        filterRegistrationBean.setOrder(-102);
        return filterRegistrationBean;
    }
}
