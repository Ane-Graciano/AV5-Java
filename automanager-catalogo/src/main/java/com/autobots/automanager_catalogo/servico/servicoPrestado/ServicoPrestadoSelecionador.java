package com.autobots.automanager_catalogo.servico.servicoPrestado;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.modelo.ServicoPrestado;
import com.autobots.automanager_catalogo.repositorio.ServicoPrestadoRepositorio;

@Service
public class ServicoPrestadoSelecionador {

    @Autowired
    private ServicoPrestadoRepositorio repositorio;

    public List<ServicoPrestado> obterServicosPrestados() {
        return repositorio.findAll();
    }

    public Optional<ServicoPrestado> obterServicoPrestado(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("O serviço com ID " + id + " não foi encontrado no sistema.");
        }
        return repositorio.findById(id);
    }
}