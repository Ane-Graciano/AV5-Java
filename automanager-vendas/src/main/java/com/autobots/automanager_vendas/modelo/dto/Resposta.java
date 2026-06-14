package com.autobots.automanager_vendas.modelo.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resposta {
    private String resposta;
    private Object dados;
}