package com.autobots.automanager_vendas.modelo.dto;

import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VendaResponseDto extends RepresentationModel<VendaResponseDto> {
    private Long id;
    private Date cadastro;
    private String identificacao;    
    private Long clienteId;  
    private Long funcionarioId;  
    private Long idEmpresa;
    private VeiculoResponseDto veiculo;
    private Set<Long> mercadoriasIds;
    private Set<Long> servicosIds;
}