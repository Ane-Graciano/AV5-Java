package com.autobots.automanager_usuarios.servicos.telefone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.repositorios.TelefoneRepositorio;

@Service
public class TelefoneApagador {
    
    @Autowired
    private TelefoneRepositorio repositorio;

    public void excluir(Long id) {
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O telefone de ID " + id + " não foi encontrado no sistema.");
        }
        repositorio.deleteById(id);
    }

    public void excluirTodos(){
        repositorio.deleteAll();
    }
}
