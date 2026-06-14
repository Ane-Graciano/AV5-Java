package com.autobots.automanager_usuarios.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EnderecoDto {
    private Long id;
    private String estado;
    
    @NotBlank(message = "Cidade em branco")
    private String cidade;
    
    private String bairro;
    
    @NotBlank(message = "Rua em branco")
    private String rua;
    
    @NotBlank(message = "Número em branco")
    private String numero;
    
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido. Use 00000-000 ou 00000000")
    private String codigoPostal;
    
    private String informacoesAdicionais;
}