package com.autobots.automanager_usuarios.servicos.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.repositorios.UsuarioRepositorio;

@Service
public class UsuarioApagador {

    @Autowired
    private UsuarioRepositorio repositorio;

    public void excluir(Long id, String usuarioLogado) {
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
        
        Usuario usuarioEspecifico = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("O usuário com ID " + id + " não foi encontrado no sistema."));
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            if (!usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                throw new RuntimeException("Acesso Negado: Vendedores só possuem permissão para excluir Clientes.");
            }
        }
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
            if (!usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE) && 
                !usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR) && 
                !usuarioEspecifico.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
                throw new RuntimeException("Acesso Negado: Gerentes só possuem permissão para excluir Clientes, Vendedores ou Gerentes.");
            }
        }
        try {
            repositorio.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir este usuário porque ele possui vínculos ou registros ativos no sistema.");
        }
    }

    public void excluirTodos(String usuarioLogado) {
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            List<Usuario> todosUsuarios = repositorio.findAll();
            for (Usuario usuario : todosUsuarios) {
                if (usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                    try {
                        repositorio.delete(usuario);
                    } catch (org.springframework.dao.DataIntegrityViolationException e) {
                        throw new RuntimeException("Não é possível excluir todos os clientes porque algum possui vínculos ativos.");
                    }
                }
            }
        } else if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_ADMIN)) {
            try {
                repositorio.deleteAll();
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                throw new RuntimeException("Não é possível limpar a base pois existem registros vinculados a outras tabelas.");
            }
        }
    }
}