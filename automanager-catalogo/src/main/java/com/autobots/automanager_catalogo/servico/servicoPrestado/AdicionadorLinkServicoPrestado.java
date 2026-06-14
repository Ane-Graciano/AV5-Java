package com.autobots.automanager_catalogo.servico.servicoPrestado;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.controle.ServicoPrestadoControle;
import com.autobots.automanager_catalogo.modelo.ServicoPrestado;
import com.autobots.automanager_catalogo.servico.AdicionadorLink;

@Service
public class AdicionadorLinkServicoPrestado implements AdicionadorLink<ServicoPrestado> {

    @Override
    public void adicionarLink(List<ServicoPrestado> lista) {
        for (ServicoPrestado servicoPrestado : lista) {
            adicionarLink(servicoPrestado);
        }
    }

    @Override
    public void adicionarLink(ServicoPrestado objeto) {
        long id = objeto.getId();
        
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .obterServicoPrestado(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .obterServicosPrestados())
                .withRel("servicosPrestados");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .atualizarServicoPrestado(id, null))
                .withRel("editar");

        Link linkEditarVarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .atualizarTodos(null))
                .withRel("editarVarios");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .excluirServicoPrestado(id))
                .withRel("excluir");

        Link linkDeletarTodos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoPrestadoControle.class)
                        .excluirTodos())
                .withRel("excluirTodos");

        objeto.add(linkProprio, linkLista, linkEditar, linkEditarVarios, linkDeletar, linkDeletarTodos);
    }
}