package com.oskin.ad_board.config;

import com.oskin.ad_board.security.JwtFilter;
import com.oskin.ad_board.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserDetailService userDetailService;
    private final JwtFilter jwtFilter;

    @Autowired
    SecurityConfig(UserDetailService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(CsrfConfigurer::disable).
                authorizeHttpRequests(
                        aut -> aut
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/auth").permitAll()
                                .requestMatchers("/reg").permitAll()
                                .requestMatchers(HttpMethod.GET, "/ad/**").permitAll()
                                .requestMatchers("/ad/**").hasRole("USER")
                                .requestMatchers("/moderation/**").hasRole("ADMIN")
                                .requestMatchers("/deal/**").hasRole("USER")
                                .requestMatchers("/dialog/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/profile/**").permitAll()
                                .requestMatchers("/profile/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/review/**").permitAll()
                                .requestMatchers("/review/**").hasRole("USER")
                                .anyRequest().permitAll()
                )
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailService)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
