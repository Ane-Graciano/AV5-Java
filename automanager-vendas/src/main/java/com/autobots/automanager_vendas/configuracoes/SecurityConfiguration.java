package com.autobots.automanager_vendas.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autobots.automanager_vendas.filtros.Autorizador;
import com.autobots.automanager_vendas.jwt.ProvedorJwt;

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
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem estado de sessão
                .authorizeHttpRequests(authorize -> authorize
                        // Caso crie um endpoint de login futuramente neste microsserviço, libere-o aqui:
                        // .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        
                        // Todas as outras rotas exigem que o usuário esteja autenticado via token
                        .anyRequest().authenticated()
                )
                // Coloca o nosso filtro customizado 'Autorizador' ANTES do filtro padrão de autenticação por formulário do Spring
                .addFilterBefore(new Autorizador(provedorJwt), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}