package com.autobots.automanager_catalogo.servico.servicoPrestado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.repositorio.ServicoPrestadoRepositorio;

@Service
public class ServicoPrestadoApagador {

    @Autowired
    private ServicoPrestadoRepositorio repositorio;

    public void excluir(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("O serviço com ID " + id + " não foi encontrado no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos() {
        repositorio.deleteAll();
    }
}