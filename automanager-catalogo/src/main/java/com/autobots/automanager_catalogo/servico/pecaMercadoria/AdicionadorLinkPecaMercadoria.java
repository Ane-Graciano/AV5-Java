package com.autobots.automanager_catalogo.servico.pecaMercadoria;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.controle.PecaMercadoriaControle;
import com.autobots.automanager_catalogo.modelo.Mercadoria;
import com.autobots.automanager_catalogo.servico.AdicionadorLink;

@Service
public class AdicionadorLinkPecaMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            adicionarLink(mercadoria);
        }
    }

    @Override
    public void adicionarLink(Mercadoria objeto) {
        long id = objeto.getId();
        
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .obterMercadoria(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .obterMercadorias())
                .withRel("mercadorias");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .atualizarMercadoria(id, null))
                .withRel("editar");

        Link linkEditarVarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .atualizarTodos(null))
                .withRel("editarVarios");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .excluirMercadoria(id))
                .withRel("excluir");

        Link linkDeletarTodos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(PecaMercadoriaControle.class)
                        .excluirTodos())
                .withRel("excluirTodos");

        objeto.add(linkProprio, linkLista, linkEditar, linkEditarVarios, linkDeletar, linkDeletarTodos);
    }
}