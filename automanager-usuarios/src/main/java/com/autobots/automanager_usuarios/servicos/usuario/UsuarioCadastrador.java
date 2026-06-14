package com.autobots.automanager_usuarios.servicos.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.modelo.dto.UsuarioDto;
import com.autobots.automanager_usuarios.repositorios.UsuarioRepositorio;

@Service
public class UsuarioCadastrador {

    @Autowired
    private UsuarioRepositorio repositorio;

    private Usuario converterUsuarioDTO(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDto.getId());
        usuario.setNome(usuarioDto.getNome());
        usuario.setNomeSocial(usuarioDto.getNomeSocial());
        usuario.setEndereco(usuarioDto.getEndereco());

        if (usuarioDto.getTelefones() != null) {
            usuario.getTelefones().addAll(usuarioDto.getTelefones());
        }
        if (usuarioDto.getDocumentos() != null) {
            usuario.getDocumentos().addAll(usuarioDto.getDocumentos());
        }
        if (usuarioDto.getEmails() != null) {
            usuario.getEmails().addAll(usuarioDto.getEmails());
        }
        if (usuarioDto.getPerfis() != null) {
            usuario.getPerfis().addAll(usuarioDto.getPerfis());
        }
        if (usuarioDto.getCredenciais() != null) {
            usuario.getCredenciais().addAll(usuarioDto.getCredenciais());
        }

        return usuario;
    }

    public Usuario cadastrar(UsuarioDto usuarioDto, String usuarioLogado, Long idEmpresa) {
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
        
        Usuario usuario = converterUsuarioDTO(usuarioDto);

        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            if (!usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                throw new RuntimeException("Acesso Negado: Vendedores só possuem permissão para cadastrar Clientes.");
            }
        }
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
            if (!usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE) && 
                !usuario.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR) && 
                !usuario.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
                throw new RuntimeException("Acesso Negado: Gerentes só possuem permissão para cadastrar Clientes, Vendedores ou Gerentes.");
            }
        }

        if (idEmpresa != null) {
            usuario.setIdEmpresa(idEmpresa);
        }

        if (usuario.getId() != null && repositorio.existsById(usuario.getId())) {
            throw new RuntimeException("ID do usuário já existe na base de dados");
        }
        
        return repositorio.save(usuario);
    }

    public List<Usuario> cadastrarVarios(List<UsuarioDto> usuariosDtos, String usuarioLogado) {
        List<Usuario> salvos = new ArrayList<>();
        for (UsuarioDto dto : usuariosDtos) {
            Usuario salvo = cadastrar(dto, usuarioLogado, dto.getIdEmpresa());
            salvos.add(salvo);
        }
        return salvos;
    }
}