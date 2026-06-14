package com.autobots.automanager_vendas.modelo.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VeiculoResponseDto extends RepresentationModel<VeiculoResponseDto> {
    private Long id;
    private String modelo;
    private String placa;
    private Long proprietarioId; 
}