package com.autobots.automanager_usuarios.servicos.telefone;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Telefone;
import com.autobots.automanager_usuarios.repositorios.TelefoneRepositorio;

@Service
public class TelefoneCadastrador {
    
    @Autowired
    private TelefoneRepositorio repositorio;

    public Telefone cadastrar(Telefone telefone){
        return repositorio.save(telefone);
    }

    public List<Telefone> cadastrarTodos(List<Telefone> telefones){
        return repositorio.saveAll(telefones);
    }
}