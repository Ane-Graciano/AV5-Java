package com.autobots.automanager_catalogo.servico.servicoPrestado;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.modelo.ServicoPrestado;
import com.autobots.automanager_catalogo.repositorio.ServicoPrestadoRepositorio;

@Service
public class ServicoPrestadoAtualizador {

    @Autowired
    private ServicoPrestadoRepositorio repositorio;

    public ServicoPrestado atualizar(Long id, ServicoPrestado servicoPrestado) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("O serviço com ID " + id + " não foi encontrado no sistema.");
        }
        
        ServicoPrestado servicoPrestadoAtualiza = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        if (servicoPrestado.getNome() != null && !servicoPrestado.getNome().isBlank()) {
            servicoPrestadoAtualiza.setNome(servicoPrestado.getNome());
        }
        if (servicoPrestado.getValor() > 0) {
            servicoPrestadoAtualiza.setValor(servicoPrestado.getValor());
        }
        if (servicoPrestado.getDescricao() != null) {
            servicoPrestadoAtualiza.setDescricao(servicoPrestado.getDescricao());
        }
        if (servicoPrestado.getIdEmpresa() != null) {
            servicoPrestadoAtualiza.setIdEmpresa(servicoPrestado.getIdEmpresa());
        }
        
        return repositorio.save(servicoPrestadoAtualiza);
    }

    public List<ServicoPrestado> atualizarTodos(List<ServicoPrestado> atualizacoes) {
        List<ServicoPrestado> atualizados = new ArrayList<>();
        for (ServicoPrestado atualizacao : atualizacoes) {
            if (atualizacao.getId() != null) {
                atualizados.add(atualizar(atualizacao.getId(), atualizacao));
            }
        }
        return atualizados;
    }
}