package com.autobots.automanager_usuarios.servicos.empresa;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.controles.EmpresaControle;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaResponseDto;
import com.autobots.automanager_usuarios.servicos.AdicionadorLink; 

@Service
public class AdicionadorLinkEmpresa implements AdicionadorLink<EmpresaResponseDto> {

    @Override
    public void adicionarLink(List<EmpresaResponseDto> lista) {
        for (EmpresaResponseDto empresa : lista) {
            adicionarLink(empresa);
        }
    }

    @Override
    public void adicionarLink(EmpresaResponseDto objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresa(id))
                .withSelfRel();

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresas())
                .withRel("empresas");

        Link linkEditar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .atualizarEmpresa(id, null))
                .withRel("editar");

        Link linkDeletar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .excluirEmpresa(id))
                .withRel("excluir");
                
        Link linkDeletarTodos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .excluirTodos())
                .withRel("excluirTodos");

        objeto.add(linkProprio, linkLista, linkEditar, linkDeletar, linkDeletarTodos);
    }
}