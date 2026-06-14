package com.autobots.automanager_vendas.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.autobots.automanager_vendas.enumeracoes.*;
import lombok.Data;

@Data
public class VeiculoDto {
    private Long id;
    private TipoVeiculo tipo;
    
    @NotBlank(message = "Modelo do veículo em branco, esse campo é obrigatório")
    private String modelo;
    
    @NotBlank(message = "Placa do veículo em branco, esse campo é obrigatório")
    @Size(min = 7, max = 7, message="A placa deve conter 7 caracteres")
    @Pattern(
        regexp= "^[a-zA-Z]{3}[0-9]{1}[a-zA-Z]{1}[0-9]{2}$",
        message="Deve seguir esse formato: LLLNLNN (ex.: ABC1D23)"
    )
    private String placa;
    
    private Long proprietarioId;
}