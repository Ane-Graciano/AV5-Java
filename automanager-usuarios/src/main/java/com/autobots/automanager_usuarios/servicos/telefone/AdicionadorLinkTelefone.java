package com.autobots.automanager_usuarios.servicos.telefone;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.controles.TelefoneControle;
import com.autobots.automanager_usuarios.modelo.Telefone;
import com.autobots.automanager_usuarios.servicos.AdicionadorLink;

@Service
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            adicionarLink(telefone);
        }
    }

    @Override
    public void adicionarLink(Telefone objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTodos())
                .withRel("telefones");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).atualizarTelefone(id, null))
                .withRel("editar");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).excluirTelefone(id))
                .withRel("excluir");

        objeto.add(linkProprio, linkLista, linkEditar, linkDeletar);
    }
}