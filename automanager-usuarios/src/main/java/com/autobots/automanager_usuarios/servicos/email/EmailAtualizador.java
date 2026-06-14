package com.autobots.automanager_usuarios.servicos.email;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Email;

@Service
public class EmailAtualizador {

    public void atualizar(Set<Email> email, Set<Email> atualizacao) {
        if (email != null && atualizacao != null && !atualizacao.isEmpty()) {
            email.clear();
            email.addAll(atualizacao);
        }
    }
}