package com.autobots.automanager_usuarios.servicos.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.modelo.dto.UsuarioDto;
import com.autobots.automanager_usuarios.repositorios.UsuarioRepositorio;
import com.autobots.automanager_usuarios.servicos.DocumentoValidador;
import com.autobots.automanager_usuarios.servicos.VerificadorNulo;
import com.autobots.automanager_usuarios.servicos.credencial.CredencialAtualizador;
import com.autobots.automanager_usuarios.servicos.documento.DocumentoAtualizador;
import com.autobots.automanager_usuarios.servicos.email.EmailAtualizador;
import com.autobots.automanager_usuarios.servicos.endereco.EnderecoAtualizador;
import com.autobots.automanager_usuarios.servicos.telefone.TelefoneAtualizador;

@Service
public class UsuarioAtualizador {

    @Autowired
    private UsuarioRepositorio repositorio;
    @Autowired
    private EnderecoAtualizador enderecoAtualizador;
    @Autowired
    private DocumentoAtualizador documentoAtualizador;
    @Autowired
    private TelefoneAtualizador telefoneAtualizador;
    @Autowired
    private EmailAtualizador emailAtualizador;
    @Autowired
    private CredencialAtualizador credenciaisAtualizador;
    @Autowired
    private DocumentoValidador validaDocumento;

    private VerificadorNulo verificador = new VerificadorNulo();

    @SuppressWarnings("unused")
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

    public Usuario atualizar(Long id, UsuarioDto atualizacaoDto, String usuarioLogado) {
        validaDocumento.documentosDuplicadoMesmoTipo(atualizacaoDto.getDocumentos());
        
        Usuario funcionarioLogado = repositorio.findByNomeUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Funcionário logado não encontrado!"));
                
        Usuario usuario = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("O usuário com ID " + id + " não foi encontrado no sistema."));
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
            if (!usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE)) {
                throw new RuntimeException("Acesso Negado: Vendedores só possuem permissão para atualizar Clientes.");
            }
        }
        
        if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
            if (!usuario.getPerfis().contains(PerfilUsuario.ROLE_CLIENTE) && 
                !usuario.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR) && 
                !usuario.getPerfis().contains(PerfilUsuario.ROLE_GERENTE)) {
                throw new RuntimeException("Acesso Negado: Gerentes só possuem permissão para atualizar Clientes, Vendedores ou Gerentes.");
            }
        }
        
        if (!verificador.verificar(atualizacaoDto.getNome())) {
            usuario.setNome(atualizacaoDto.getNome());
        }
        if (!verificador.verificar(atualizacaoDto.getNomeSocial())) {
            usuario.setNomeSocial(atualizacaoDto.getNomeSocial());
        }
        if (atualizacaoDto.getEndereco() != null) {
            enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacaoDto.getEndereco());
        }

        if (atualizacaoDto.getDocumentos() != null && !atualizacaoDto.getDocumentos().isEmpty()) {
            documentoAtualizador.atualizar(usuario.getDocumentos(), atualizacaoDto.getDocumentos());
        }

        if (atualizacaoDto.getTelefones() != null && !atualizacaoDto.getTelefones().isEmpty()) {
            telefoneAtualizador.atualizar(usuario.getTelefones(), atualizacaoDto.getTelefones());
        }

        if (atualizacaoDto.getEmails() != null && !atualizacaoDto.getEmails().isEmpty()) {
            emailAtualizador.atualizar(usuario.getEmails(), atualizacaoDto.getEmails());
        }

        if (atualizacaoDto.getCredenciais() != null && !atualizacaoDto.getCredenciais().isEmpty()) {
            credenciaisAtualizador.atualizar(usuario.getCredenciais(), atualizacaoDto.getCredenciais());
        }

        if (atualizacaoDto.getPerfis() != null && !atualizacaoDto.getPerfis().isEmpty()) {
            if (funcionarioLogado.getPerfis().contains(PerfilUsuario.ROLE_VENDEDOR)) {
                throw new RuntimeException("Acesso Negado: Vendedores não têm permissão para alterar os perfis de acesso de um usuário.");
            }
            usuario.getPerfis().clear();
            usuario.getPerfis().addAll(atualizacaoDto.getPerfis());
        }
        
        return repositorio.save(usuario);
    }
}