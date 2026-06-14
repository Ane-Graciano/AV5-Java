package com.autobots.automanager_vendas.servicos.veiculo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.modelo.Veiculo;
import com.autobots.automanager_vendas.modelo.dto.VeiculoDto;
import com.autobots.automanager_vendas.repositorio.VeiculoRepositorio;

@Service
public class VeiculoCadastrador {

    @Autowired
    private VeiculoRepositorio repositorio;

    private Veiculo converterVeiculoDTO(VeiculoDto veiculoDto) {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoDto.getId());
        veiculo.setTipo(veiculoDto.getTipo());
        veiculo.setModelo(veiculoDto.getModelo());
        veiculo.setPlaca(veiculoDto.getPlaca());
        veiculo.setProprietarioId(veiculoDto.getProprietarioId());
        return veiculo;
    }

    public Veiculo cadastrar(VeiculoDto veiculoDto) {
        if (veiculoDto.getId() != null && repositorio.existsById(veiculoDto.getId())) {
            throw new RuntimeException("ID do veículo já existe na base de dados");
        }
        return repositorio.save(converterVeiculoDTO(veiculoDto));
    }

    public List<Veiculo> cadastrarVarios(List<VeiculoDto> veiculosDtos) {
        List<Veiculo> veiculos = new ArrayList<>();
        for (VeiculoDto dto : veiculosDtos) {
            veiculos.add(repositorio.save(converterVeiculoDTO(dto)));
        }
        return veiculos;
    }
}
