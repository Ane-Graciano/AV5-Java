package com.autobots.automanager_usuarios.servicos.endereco;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.repositorios.EnderecoRepositorio;

@Service
public class EnderecoSelecionador {
    
    @Autowired
    private EnderecoRepositorio repositorio;

    public List<Endereco> obterTodos(){
        return repositorio.findAll();
    }

    public Optional<Endereco> obterEndereco(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O endereço de ID " + id + " não foi encontrado no sistema.");
        }
        return repositorio.findById(id);
    }
}