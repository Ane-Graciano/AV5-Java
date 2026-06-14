package com.autobots.automanager_usuarios.servicos.endereco;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.controles.EnderecoControle;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.servicos.AdicionadorLink;

@Service
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

    @Override
    public void adicionarLink(List<Endereco> lista) {
        for (Endereco endereco : lista) {
            adicionarLink(endereco);
        }
    }

    @Override
    public void adicionarLink(Endereco objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEndereco(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obteerTodos())
                .withRel("enderecos");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).atualizarEndereco(id, null))
                .withRel("editar");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).excluirEndereco(id))
                .withRel("excluir");

        objeto.add(linkProprio, linkLista, linkEditar, linkDeletar);
    }
}