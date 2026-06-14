package com.autobots.automanager_usuarios.servicos.documento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Documento;
import com.autobots.automanager_usuarios.repositorios.DocumentoRepositorio;

@Service
public class DocumentoCadastrador {
    
    @Autowired
    private DocumentoRepositorio repositorio;

    public Documento cadastrar(Documento documento){
        return repositorio.save(documento);
    }

    public List<Documento> cadastrarTodos(List<Documento> documentos){
        return repositorio.saveAll(documentos);
    }
}