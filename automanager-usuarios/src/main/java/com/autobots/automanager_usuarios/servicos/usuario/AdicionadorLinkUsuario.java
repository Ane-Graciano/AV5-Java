package com.autobots.automanager_usuarios.servicos.usuario;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.controles.UsuarioControle;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.servicos.AdicionadorLink;
import com.autobots.automanager_usuarios.servicos.documento.AdicionadorLinkDocumento;
import com.autobots.automanager_usuarios.servicos.endereco.AdicionadorLinkEndereco;
import com.autobots.automanager_usuarios.servicos.telefone.AdicionadorLinkTelefone;

@Service
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Autowired
    private AdicionadorLinkDocumento adicionadorLinkDocumento;
    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;
    @Autowired 
    private AdicionadorLinkEndereco adicionadorLinkEndereco;

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            adicionarLink(usuario);
        }
    }

    @Override
    public void adicionarLink(Usuario objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(id, null))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios(null))
                .withRel("usuarios");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).atualizarUsuario(id, null, null))
                .withRel("editar");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).excluirUsuario(id, null))
                .withRel("excluir");

        Link linkDeletarTodos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).excluirTodos(null))
                .withRel("excluirTodos");

        objeto.add(linkProprio, linkLista, linkEditar, linkDeletar, linkDeletarTodos);
                        
        if (objeto.getDocumentos() != null) {
            objeto.getDocumentos().forEach(doc -> adicionadorLinkDocumento.adicionarLink(doc));
        }
        if (objeto.getTelefones() != null) {
            objeto.getTelefones().forEach(tel -> adicionadorLinkTelefone.adicionarLink(tel));
        }
        if (objeto.getEndereco() != null) {
            adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
        }
    }
}