package com.arpith.mockpay.identityservice.config;

import com.arpith.mockpay.identityservice.enumeration.Authorities;
import com.arpith.mockpay.identityservice.filter.JwtAuthenticationEntryPoint;
import com.arpith.mockpay.identityservice.filter.JwtRequestFilter;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, @Autowired SecuredUserService securedUserService,
                                                   @Autowired JwtRequestFilter jwtRequestFilter, @Autowired JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorise ->
                        authorise
                                .requestMatchers(HttpMethod.PATCH, "/v1/user/update/**").hasAuthority(Authorities.UPDATE_USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/v1/user/delete/**").hasAuthority(Authorities.DELETE_USER.name())
                                .requestMatchers(HttpMethod.GET, "/v1/user/fetch/**").hasAuthority(Authorities.GET_USER.name())
                                .requestMatchers(HttpMethod.GET, "v1/token/generate/**").hasAuthority(Authorities.REQUEST_TOKEN.name())
                                .requestMatchers(HttpMethod.POST, "v1/token/verify/**").hasAuthority(Authorities.VERIFY_TOKEN.name())
                                .requestMatchers(HttpMethod.POST, "/v1/user/create/**").permitAll()
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider(securedUserService))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
