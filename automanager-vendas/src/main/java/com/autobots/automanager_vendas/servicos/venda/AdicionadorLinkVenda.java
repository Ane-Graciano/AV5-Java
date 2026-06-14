package com.autobots.automanager_vendas.servicos.venda;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_vendas.controles.VendaControle;
import com.autobots.automanager_vendas.modelo.dto.VendaResponseDto;

@Service
public class AdicionadorLinkVenda {

    public void adicionarLink(List<VendaResponseDto> lista) {
        for (VendaResponseDto venda : lista) {
            adicionarLink(venda);
        }
    }

    public void adicionarLink(VendaResponseDto objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id)).withSelfRel();
        Link linkLista = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withRel("vendas");
        Link linkDeletar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).excluirVenda(id)).withRel("excluir");

        objeto.add(linkProprio, linkLista, linkDeletar);
    }
}