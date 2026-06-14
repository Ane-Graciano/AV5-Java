package com.autobots.automanager_usuarios.servicos.credencial;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Credencial;
import com.autobots.automanager_usuarios.modelo.CredencialUsuarioSenha;
import java.util.Set;

@Service
public class CredencialAtualizador {

    private BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    public void atualizar(Set<Credencial> credenciais, Set<Credencial> atualizacao) {
        if (credenciais != null && atualizacao != null && !atualizacao.isEmpty()) {
            for (Credencial credencialNova : atualizacao) {
                if (credencialNova instanceof CredencialUsuarioSenha) {
                    CredencialUsuarioSenha novaSenha = (CredencialUsuarioSenha) credencialNova;
                    for (Credencial antiga : credenciais) {
                        if (antiga instanceof CredencialUsuarioSenha) {
                            CredencialUsuarioSenha antigaSenhaBanco = (CredencialUsuarioSenha) antiga;
                            if (novaSenha.getNomeUsuario() != null && !novaSenha.getNomeUsuario().trim().isEmpty()) {
                                antigaSenhaBanco.setNomeUsuario(novaSenha.getNomeUsuario());
                                if (novaSenha.getSenha() != null && !novaSenha.getSenha().trim().isEmpty()) {
                                    String senhaCriptografada = codificador.encode(novaSenha.getSenha());
                                    antigaSenhaBanco.setSenha(senhaCriptografada);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}