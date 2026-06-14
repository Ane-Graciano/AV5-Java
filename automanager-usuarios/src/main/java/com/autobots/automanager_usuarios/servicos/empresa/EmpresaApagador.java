package com.autobots.automanager_usuarios.servicos.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.repositorios.EmpresaRepositorio;

@Service
public class EmpresaApagador {

    @Autowired
    private EmpresaRepositorio repositorio;

    public void excluir(Long id) {
        if(!repositorio.existsById(id)){
            throw new RuntimeException("A empresa com ID " + id + " não foi encontrada no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos() {
        repositorio.deleteAll();
    }
}