package com.autobots.automanager_vendas.servicos.venda;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.modelo.Venda;
import com.autobots.automanager_vendas.modelo.dto.VeiculoResponseDto;
import com.autobots.automanager_vendas.modelo.dto.VendaResponseDto;
import com.autobots.automanager_vendas.repositorio.VendaRepositorio;

@Service
public class VendaSelecionador {

    @Autowired
    private VendaRepositorio repositorio;

    public VendaResponseDto converterParaResponse(Venda venda) {
        VendaResponseDto dto = new VendaResponseDto();
        dto.setId(venda.getId());
        dto.setCadastro(venda.getCadastro());
        dto.setIdentificacao(venda.getIdentificacao());
        dto.setClienteId(venda.getClienteId());
        dto.setFuncionarioId(venda.getFuncionarioId());
        dto.setIdEmpresa(venda.getIdEmpresa());
        dto.setMercadoriasIds(venda.getMercadoriasIds());
        dto.setServicosIds(venda.getServicosIds());

        if (venda.getVeiculo() != null) {
            VeiculoResponseDto veiculoDto = new VeiculoResponseDto();
            veiculoDto.setId(venda.getVeiculo().getId());
            veiculoDto.setModelo(venda.getVeiculo().getModelo());
            veiculoDto.setPlaca(venda.getVeiculo().getPlaca());
            veiculoDto.setProprietarioId(venda.getVeiculo().getProprietarioId());
            dto.setVeiculo(veiculoDto);
        }

        return dto;
    }

    public List<VendaResponseDto> converterListaParaResponse(List<Venda> vendas) {
        return vendas.stream().map(this::converterParaResponse).collect(Collectors.toList());
    }

    public List<VendaResponseDto> obterVendas() {
        return repositorio.findAll().stream().map(this::converterParaResponse).collect(Collectors.toList());
    }

    public VendaResponseDto obterVenda(Long id) {
        Venda venda = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("A venda com ID " + id + " não foi encontrada."));
        return converterParaResponse(venda);
    }
}