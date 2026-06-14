package com.autobots.automanager_usuarios.jwt;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class ProvedorJwt {

    @Value("${jwt.secret}")
    private String assinatura;

    @Value("${jwt.expiration}")
    private Long duracao;

    public String proverJwt(String nomeUsuario, List<String> perfis) {
        return Jwts.builder()
                .setSubject(nomeUsuario)
                .claim("perfis", perfis) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duracao))
                .signWith(SignatureAlgorithm.HS512, assinatura.getBytes())
                .compact();
    }

    public boolean validarToken(String jwt) {
        try {
            Claims claims = obterReivindicacoes(jwt);
            if (claims != null) {
                String nomeUsuario = claims.getSubject();
                Date dataExpiracao = claims.getExpiration();
                Date agora = new Date();
                return nomeUsuario != null && dataExpiracao != null && agora.before(dataExpiracao);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String obterNomeUsuario(String jwt) {
        Claims claims = obterReivindicacoes(jwt);
        return claims != null ? claims.getSubject() : null;
    }

    @SuppressWarnings("unchecked")
    public List<String> obterPerfis(String jwt) {
        Claims claims = obterReivindicacoes(jwt);
        return claims != null ? claims.get("perfis", List.class) : null;
    }

    private Claims obterReivindicacoes(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(assinatura.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}