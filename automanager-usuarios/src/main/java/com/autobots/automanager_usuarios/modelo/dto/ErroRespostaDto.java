package com.autobots.automanager_usuarios.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroRespostaDto {
    private String erro;
    private String mensagem;
}
