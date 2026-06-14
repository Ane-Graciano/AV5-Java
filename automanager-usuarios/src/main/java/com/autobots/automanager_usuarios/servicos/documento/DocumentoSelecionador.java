package com.autobots.automanager_usuarios.servicos.documento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Documento;
import com.autobots.automanager_usuarios.repositorios.DocumentoRepositorio;

@Service
public class DocumentoSelecionador {
    
    @Autowired
    private DocumentoRepositorio repositorio;

    public Optional<Documento> obeterDocumento(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O documento de ID " + id + " não foi encontrado no sistema.");
        }
        return repositorio.findById(id);
    }

    public List<Documento> obterTodos(){
        return repositorio.findAll();
    }
}
