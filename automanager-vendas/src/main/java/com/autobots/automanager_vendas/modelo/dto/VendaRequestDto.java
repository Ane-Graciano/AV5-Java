package com.autobots.automanager_vendas.modelo.dto;

import java.util.Set;

import lombok.Data;

@Data
public class VendaRequestDto {
    private String identificacao;
    private Long clienteId;     
    private Long funcionarioId;
    private Long veiculoId;     
    private Set<Long> mercadoriasIds;
    private Set<Long> servicosIds;
    private Long idEmpresa;
}