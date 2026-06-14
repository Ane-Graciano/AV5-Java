package com.autobots.automanager_vendas.servicos.veiculo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import com.autobots.automanager_vendas.controles.VeiculoControle;
import com.autobots.automanager_vendas.modelo.dto.VeiculoResponseDto;

@Service
public class AdicionadorLinkVeiculo {

    public void adicionarLink(List<VeiculoResponseDto> lista) {
        for (VeiculoResponseDto veiculo : lista) {
            adicionarLink(veiculo);
        }
    }

    public void adicionarLink(VeiculoResponseDto objeto) {
        long id = objeto.getId();
        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id)).withSelfRel();
        Link linkLista = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos");
        Link linkDeletar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).excluirVeiculo(id)).withRel("excluir");

        objeto.add(linkProprio, linkLista, linkDeletar);
    }
}