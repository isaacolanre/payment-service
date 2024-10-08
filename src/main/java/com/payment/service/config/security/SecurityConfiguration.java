package com.payment.service.config.security;

import com.payment.service.config.security.filters.AppUserRefreshTokenFilter;
import com.payment.service.config.security.filters.JwtAuthenticationFilter;
import com.payment.service.config.security.filters.UsernamePasswordAuthenticationFilter;
import com.payment.service.config.security.providers.AppUsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
    private final AppUsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final AppUserRefreshTokenFilter userRefreshTokenFilter;
    private String[] permittedRoutes = List.of("/", "/**").toArray(new String[0]);

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter
            usernamePasswordAuthenticationFilter, AppUserRefreshTokenFilter userRefreshTokenFilter,
                                 AppUsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.usernamePasswordAuthenticationFilter = usernamePasswordAuthenticationFilter;
        this.userRefreshTokenFilter = userRefreshTokenFilter;
        this.usernamePasswordAuthenticationProvider = usernamePasswordAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.POST,  "/api/v1/signup").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/signup").permitAll()
                                .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(userRefreshTokenFilter, JwtAuthenticationFilter.class)
                .addFilterAfter(usernamePasswordAuthenticationFilter, JwtAuthenticationFilter.class);

        http.authenticationProvider(usernamePasswordAuthenticationProvider);
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}


