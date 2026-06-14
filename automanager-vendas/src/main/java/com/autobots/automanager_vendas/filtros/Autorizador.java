package com.autobots.automanager_vendas.filtros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.autobots.automanager_vendas.jwt.ProvedorJwt;

public class Autorizador extends OncePerRequestFilter {

    private ProvedorJwt provedorJwt;

    public Autorizador(ProvedorJwt provedorJwt) {
        this.provedorJwt = provedorJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null && provedorJwt.validarToken(token)) {
            String nomeUsuario = provedorJwt.obterNomeUsuario(token);
            List<String> perfis = provedorJwt.obterPerfis(token);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (perfis != null) {
                perfis.forEach(perfil -> authorities.add(new SimpleGrantedAuthority(perfil)));
            }

            UsernamePasswordAuthenticationToken autenticacao = 
                    new UsernamePasswordAuthenticationToken(nomeUsuario, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String cacheObjeto = request.getHeader("Authorization");
        if (cacheObjeto == null || cacheObjeto.isEmpty() || !cacheObjeto.startsWith("Bearer ")) {
            return null;
        }
        return cacheObjeto.substring(7);
    }
}