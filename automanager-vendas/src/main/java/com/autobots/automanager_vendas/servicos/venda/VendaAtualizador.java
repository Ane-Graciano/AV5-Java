package com.autobots.automanager_vendas.servicos.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.modelo.Venda;
import com.autobots.automanager_vendas.modelo.dto.VendaRequestDto;
import com.autobots.automanager_vendas.repositorio.VendaRepositorio;

@Service
public class VendaAtualizador {

    @Autowired
    private VendaRepositorio repositorio;

    public Venda atualizar(Long id, VendaRequestDto atualizacaoDto) {
        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada no sistema."));

        if (atualizacaoDto.getIdentificacao() != null) {
            venda.setIdentificacao(atualizacaoDto.getIdentificacao());
        }

        return repositorio.save(venda);
    }
}