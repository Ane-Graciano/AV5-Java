package com.autobots.automanager_vendas.servicos.veiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.modelo.Veiculo;
import com.autobots.automanager_vendas.modelo.dto.VeiculoDto;      
import com.autobots.automanager_vendas.repositorio.VeiculoRepositorio; 

@Service
public class VeiculoAtualizador {

    @Autowired
    private VeiculoRepositorio repositorio; 
    
    public Veiculo atualizar(Long id, VeiculoDto atualizacaoDto) {
        Veiculo veiculo = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));

        if (atualizacaoDto.getTipo() != null) veiculo.setTipo(atualizacaoDto.getTipo());
        if (atualizacaoDto.getModelo() != null) veiculo.setModelo(atualizacaoDto.getModelo());
        if (atualizacaoDto.getPlaca() != null) veiculo.setPlaca(atualizacaoDto.getPlaca());
        if (atualizacaoDto.getProprietarioId() != null) veiculo.setProprietarioId(atualizacaoDto.getProprietarioId());

        return repositorio.save(veiculo);
    }
}