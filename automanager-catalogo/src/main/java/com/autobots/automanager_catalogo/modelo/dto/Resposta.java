package com.autobots.automanager_catalogo.modelo.dto;

import lombok.Data;

@Data
public class Resposta {
    private String message; // Mantendo o padrão do atributo de retorno do seu monólito
    private Object dados;

    public Resposta(String message, Object dados) {
        this.message = message;
        this.dados = dados;
    }
}