package com.autobots.automanager_vendas.servicos.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_vendas.repositorio.VendaRepositorio;

@Service
public class VendaApagador {

    @Autowired
    private VendaRepositorio repositorio;

    public void excluir(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("A venda com ID " + id + " não existe.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos() {
        repositorio.deleteAll();
    }
}