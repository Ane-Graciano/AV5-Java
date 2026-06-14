package com.autobots.automanager_usuarios.modelo.dto;

import java.util.HashSet;
import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import com.autobots.automanager_usuarios.modelo.*;
import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioDto {
    private Long id;
    
    @NotBlank(message = "Nome do usuário em branco")
    private String nome;
    
    private String nomeSocial;
    
    @Valid
    private Set<PerfilUsuario> perfis = new HashSet<>();
    
    @Valid
    private Set<Documento> documentos = new HashSet<>();
    
    @Valid
    private Endereco endereco;
    
    @Valid
    private Set<Telefone> telefones = new HashSet<>();
    
    @Valid
    private Set<Email> emails = new HashSet<>();
    
    @Valid
    private Set<Credencial> credenciais = new HashSet<>();
    
    private Long idEmpresa;

}



