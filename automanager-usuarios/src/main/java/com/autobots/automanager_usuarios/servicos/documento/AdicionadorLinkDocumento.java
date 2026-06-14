package com.autobots.automanager_usuarios.servicos.documento;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.controles.DocumentoControle;
import com.autobots.automanager_usuarios.modelo.Documento;
import com.autobots.automanager_usuarios.servicos.AdicionadorLink;

@Service
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

    @Override
    public void adicionarLink(List<Documento> lista) {
        for (Documento documento : lista) {
            adicionarLink(documento);
        }
    }

    @Override
    public void adicionarLink(Documento objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obeterDocumento(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterTodos())
                .withRel("documentos");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).atualizarDocumento(id, null))
                .withRel("editar");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).excluirDocumento(id))
                .withRel("excluir");

        objeto.add(linkProprio, linkLista, linkEditar, linkDeletar);
    }
}