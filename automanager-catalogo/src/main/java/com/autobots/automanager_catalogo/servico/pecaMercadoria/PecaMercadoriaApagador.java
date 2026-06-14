package com.autobots.automanager_catalogo.servico.pecaMercadoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.repositorio.MercadoriaRepositorio;

@Service
public class PecaMercadoriaApagador {

    @Autowired
    private MercadoriaRepositorio repositorio;

    public void excluir(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("A mercadoria com ID " + id + " não foi encontrada no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos() {
        repositorio.deleteAll();
    }
}