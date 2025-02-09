package com.jwt.configuration;

import com.jwt.filter.JwtFilter;
import com.jwt.service.impl.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // to say that this is the configuration file to spring
@EnableWebSecurity // for implementation of configuration instead of default config (Go wih this configuration instead of default)
public class SecurityConfig {

    @Autowired
    private AppUserDetailsService userDetailService;

    @Autowired
    private JwtFilter jwtFilter;

    public SecurityConfig(AppUserDetailsService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/add", "/user/login").permitAll()
                        .anyRequest().authenticated()) // authentication is applied on this line (by default we follow password authentication)
//                .formLogin(Customizer.withDefaults()) // to work with default login.html form
                .httpBasic(Customizer.withDefaults()) // to work with authentication on postman
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // it will create new session for every request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //
//                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // authentication provider for database
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // for no password encoder
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // for password encoder
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // manager talks to provider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

