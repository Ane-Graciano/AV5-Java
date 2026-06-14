package com.autobots.automanager_catalogo.servico.pecaMercadoria;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.modelo.Mercadoria;
import com.autobots.automanager_catalogo.repositorio.MercadoriaRepositorio;

@Service
public class PecaMercadoriaSelecionador {

    @Autowired
    private MercadoriaRepositorio repositorio;

    public List<Mercadoria> obterMercadorias() {
        return repositorio.findAll();
    }

    public Optional<Mercadoria> obterMercadoria(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("A mercadoria com ID " + id + " não foi encontrada no sistema.");
        }
        return repositorio.findById(id);
    }
}