package com.autobots.automanager_vendas.servicos.veiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.repositorio.VeiculoRepositorio;

@Service
public class VeiculoApagador {

    @Autowired
    private VeiculoRepositorio repositorio;

    public void excluir(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("O veículo com ID " + id + " não foi encontrado.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos() {
        repositorio.deleteAll();
    }
}
