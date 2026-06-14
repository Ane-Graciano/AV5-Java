package com.autobots.automanager_usuarios.servicos.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.repositorios.DocumentoRepositorio;

@Service
public class DocumentoApagador {

    @Autowired
    private DocumentoRepositorio repositorio;
    
    public void excluir(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O documento de ID " + id + " não foi encontrado no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos(){
        repositorio.deleteAll();
    }
}