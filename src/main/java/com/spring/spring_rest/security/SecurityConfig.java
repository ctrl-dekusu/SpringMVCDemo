package com.spring.spring_rest.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.spring_rest.service.CustomUserDetailService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

/**
 * Spring Security configuration class defining HTTP security rules, 
 * password encoding, and authentication management.
 */
@EnableWebSecurity 
@Configuration 
public class SecurityConfig {

  private CustomUserDetailService userDetailsService;

  /**
   * Constructs a {@code SecurityConfig} with the specified 
   * {@link CustomUserDetailService}.
   *
   * @param userDetailService the custom user details service 
   * used for loading user data
   */
  public SecurityConfig(CustomUserDetailService userDetailService) {
    this.setUserDetailsService(userDetailsService); 
  }
  
  /**
   * Sets the {@link CustomUserDetailService} instance.
   *
   * @param userDetailsService the custom user details service to set
   */
  public void setUserDetailsService(
    CustomUserDetailService userDetailsService
  ) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    return security
            .cors(cors -> cors.configure(security))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
              .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> auth
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
              .requestMatchers("/api/students/**")
              .hasAnyAuthority("USER", "ADMIN")
              .anyRequest()
              .authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
  }

  /**
   * Provides a {@link BCryptPasswordEncoder} bean for hashing user 
   * passwords.
   *
   * @return a new {@link BCryptPasswordEncoder} instance
   */
  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Exposes the {@link AuthenticationManager} bean from the Spring 
   * {@link AuthenticationConfiguration}.
   *
   * @param authentication the {@link AuthenticationConfiguration} instance
   * @return the configured {@link AuthenticationManager}
   * @throws Exception if an error occurs while retrieving the 
   * authentication manager
   */
  @Bean
  AuthenticationManager authenticationManager(
    AuthenticationConfiguration authentication
  ) throws Exception {
    return authentication.getAuthenticationManager();
  }

  @Bean 
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(
      Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000")
    );
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    );
    configuration.setAllowedHeaders(
      Arrays.asList("Authorization", "Cache-Control", "Content-Type")
    );
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
