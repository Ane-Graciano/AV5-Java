package com.autobots.automanager_catalogo.servico.pecaMercadoria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_catalogo.modelo.Mercadoria;
import com.autobots.automanager_catalogo.repositorio.MercadoriaRepositorio;

@Service
public class PecaMercadoriaAtualizador {

    @Autowired
    private MercadoriaRepositorio repositorio;

    public Mercadoria atualizar(Long id, Mercadoria mercadoria) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("A mercadoria com ID " + id + " não foi encontrada no sistema.");
        }
        
        Mercadoria mercadoriaAtualiza = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
        
        if (mercadoria.getValidade() != null) {
            mercadoriaAtualiza.setValidade(mercadoria.getValidade());
        }
        if (mercadoria.getFabricao() != null) {
            mercadoriaAtualiza.setFabricao(mercadoria.getFabricao());
        }
        if (mercadoria.getCadastro() != null) {
            mercadoriaAtualiza.setCadastro(mercadoria.getCadastro());
        }
        if (mercadoria.getNome() != null && !mercadoria.getNome().isBlank()) {
            mercadoriaAtualiza.setNome(mercadoria.getNome());
        }
        if (mercadoria.getQuantidade() >= 0) {
            mercadoriaAtualiza.setQuantidade(mercadoria.getQuantidade());
        }
        if (mercadoria.getValor() > 0) {
            mercadoriaAtualiza.setValor(mercadoria.getValor());
        }
        if (mercadoria.getDescricao() != null) {
            mercadoriaAtualiza.setDescricao(mercadoria.getDescricao());
        }
        if (mercadoria.getIdEmpresa() != null) {
            mercadoriaAtualiza.setIdEmpresa(mercadoria.getIdEmpresa());
        }
        
        return repositorio.save(mercadoriaAtualiza);
    }

    public List<Mercadoria> atualizarTodos(List<Mercadoria> atualizacoes) {
        List<Mercadoria> atualizados = new ArrayList<>();
        for (Mercadoria atualizacao : atualizacoes) {
            if (atualizacao.getId() != null) {
                atualizados.add(atualizar(atualizacao.getId(), atualizacao));
            }
        }
        return atualizados;
    }
}