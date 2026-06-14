package com.autobots.automanager_usuarios.modelo.dto;

import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.modelo.Telefone;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmpresaResponseDto extends RepresentationModel<EmpresaResponseDto> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String data_cadastro;
    private Set<Telefone> telefones;
    private Endereco endereco;
    private Set<UsuarioResponseDto> usuarios; // Certifique-se de que o DTO de resposta do Usuário já existe nesse pacote
}