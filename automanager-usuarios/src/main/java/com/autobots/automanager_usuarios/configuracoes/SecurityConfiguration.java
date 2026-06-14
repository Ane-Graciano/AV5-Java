package com.autobots.automanager_usuarios.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autobots.automanager_usuarios.filtros.Autorizador;
import com.autobots.automanager_usuarios.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfiguration {

    private final ProvedorJwt provedorJwt;

    public SecurityConfiguration(ProvedorJwt provedorJwt) {
        this.provedorJwt = provedorJwt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new Autorizador(provedorJwt), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

   @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, 
                                                       PasswordEncoder passwordEncoder, 
                                                       org.springframework.security.core.userdetails.UserDetailsService userDetailsService) throws Exception {
        org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
        
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}