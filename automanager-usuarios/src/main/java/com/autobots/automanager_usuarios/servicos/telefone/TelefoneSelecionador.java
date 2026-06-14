package com.autobots.automanager_usuarios.servicos.telefone;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Telefone;
import com.autobots.automanager_usuarios.repositorios.TelefoneRepositorio;

@Service
public class TelefoneSelecionador {

    @Autowired
    private TelefoneRepositorio repositorio;

    public List<Telefone> obterTodos(){
        return repositorio.findAll();
    }

    public Optional<Telefone> obterTelefone(Long id){
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O telefone de ID " + id + " não foi encontrado no sistema.");
        }
        return repositorio.findById(id);
    }
}
