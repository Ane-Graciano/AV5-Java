package com.autobots.automanager_usuarios.excecao.personalizado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroDuplicado extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String mensagem;

    public CadastroDuplicado(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}