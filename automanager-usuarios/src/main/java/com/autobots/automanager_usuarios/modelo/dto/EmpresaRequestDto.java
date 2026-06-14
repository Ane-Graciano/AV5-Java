package com.autobots.automanager_usuarios.modelo.dto;

import java.util.Set;

import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.modelo.Telefone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaRequestDto {
    private Long id;

    @NotBlank(message = "Razão social em branco, campo obrigatório!")
    private String razaoSocial;
    
    private String nomeFantasia;
    private Set<Telefone> telefones;

    @Valid
    private Endereco endereco;
}