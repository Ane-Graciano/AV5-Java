package com.autobots.automanager_vendas.servicos.veiculo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.modelo.Veiculo;
import com.autobots.automanager_vendas.modelo.dto.VeiculoResponseDto;
import com.autobots.automanager_vendas.repositorio.VeiculoRepositorio;

@Service
public class VeiculoSelecionador {

    @Autowired
    private VeiculoRepositorio repositorio;

    public VeiculoResponseDto converterParaResponse(Veiculo veiculo) {
        VeiculoResponseDto dto = new VeiculoResponseDto();
        dto.setId(veiculo.getId());
        dto.setModelo(veiculo.getModelo());
        dto.setPlaca(veiculo.getPlaca());
        dto.setProprietarioId(veiculo.getProprietarioId());
        return dto;
    }

    public List<VeiculoResponseDto> converterListaParaResponse(List<Veiculo> veiculos) {
        return veiculos.stream().map(this::converterParaResponse).collect(Collectors.toList());
    }

    public List<Veiculo> obterVeiculos() {
        return repositorio.findAll();
    }

    public Veiculo obterVeiculo(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + id));
    }
}