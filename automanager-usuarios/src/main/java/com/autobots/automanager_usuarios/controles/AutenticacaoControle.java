package com.autobots.automanager_usuarios.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.autobots.automanager_usuarios.jwt.ProvedorJwt;

class RequisicaoLogin {
    private String nomeUsuario;
    private String senha;

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

@RestController
public class AutenticacaoControle {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

   @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequisicaoLogin requisicao) {
        
        UsernamePasswordAuthenticationToken dadosLogin = 
            new UsernamePasswordAuthenticationToken(requisicao.getNomeUsuario(), requisicao.getSenha());
        
        Authentication authentication = authenticationManager.authenticate(dadosLogin);
        
        String nomeUsuario = authentication.getName();
        
        List<String> perfis = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();
        
        String token = provedorJwt.proverJwt(nomeUsuario, perfis);
        
        return ResponseEntity.ok(token);
    }
}
