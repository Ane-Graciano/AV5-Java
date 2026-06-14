package com.autobots.automanager_usuarios.servicos.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.repositorios.UsuarioRepositorio;

@Service
public class UsuarioSelecionador {

    @Autowired
    private UsuarioRepositorio repositorio;

    public List<Usuario> obterUsuarios(String usuarioLogado) {
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
        
        List<Usuario> todosUsuarios = repositorio.findAll();
        List<Usuario> usuariosFiltrados = new ArrayList<>();
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            for (Usuario usuario : todosUsuarios) {
                if (usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                    usuariosFiltrados.add(usuario);
                }
            }
        } else if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
            for (Usuario usuario : todosUsuarios) {
                if (usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE) || 
                    usuario.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR) || 
                    usuario.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
                    usuariosFiltrados.add(usuario);
                }
            }
        } else if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_ADMIN)) {
            usuariosFiltrados.addAll(todosUsuarios);
        }
        return usuariosFiltrados;
    }

    public Usuario obterUsuario(Long id, String usuarioLogado) {
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
        
        Usuario usuarioEspecifico = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não foi encontrado no sistema."));
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            if (!usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                throw new RuntimeException("Acesso Negado: Vendedores só possuem permissão para visualizar dados de Clientes.");
            }
        }
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
            if (!usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE) && 
                !usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR) && 
                !usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
                throw new RuntimeException("Acesso Negado: Gerentes só possuem permissão para visualizar dados de Clientes, Vendedores ou Gerentes.");
            }
        }

        return usuarioEspecifico;
    }

    public Usuario obterPorNome(String nomeUsuario) {
        return repositorio.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Perfil do usuário não encontrado no banco de dados."));
    }
}