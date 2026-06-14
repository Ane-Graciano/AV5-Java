package com.autobots.automanager_usuarios.modelo.dto;
import java.util.Set;
import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager_usuarios.modelo.*;
import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDto extends RepresentationModel<UsuarioResponseDto>{
    private Long id;
    private String nome;
    private String nomeSocial;
    private Long idEmpresa;
    private Set<PerfilUsuario> perfis;
    private Set<Email> emails;
}
