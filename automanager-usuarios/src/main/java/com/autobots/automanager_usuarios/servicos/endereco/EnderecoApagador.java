package com.autobots.automanager_usuarios.servicos.endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.repositorios.EnderecoRepositorio;

@Service
public class EnderecoApagador {
     
    @Autowired
    private EnderecoRepositorio repositorio;

    public void excluir(Long id) {
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O endereço de ID " + id + " não foi encontrado no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos(){
        repositorio.deleteAll();
    }
}
