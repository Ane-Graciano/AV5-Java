package com.autobots.automanager_usuarios.servicos.telefone;

import java.util.Collection;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Telefone;
import com.autobots.automanager_usuarios.repositorios.TelefoneRepositorio;
import com.autobots.automanager_usuarios.servicos.VerificadorNulo;

@Service
public class TelefoneAtualizador {

    private VerificadorNulo verificador = new VerificadorNulo();

    @Autowired
    private TelefoneRepositorio repositorio;

    public void atualizar(Telefone telefone, Telefone atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(Collection<Telefone> telefones, Collection<Telefone> atualizacoes) {
        if (telefones == null || atualizacoes == null) return;
        for (Telefone atualizacao : atualizacoes) {
            for (Telefone telefone : telefones) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(telefone.getId())) {
                    atualizar(telefone, atualizacao);
                }
            }
        }
    }

    public Telefone atualizarTelefoneId(Long id, Telefone atualizacao) {
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O telefone de ID " + id + " não foi encontrado no sistema.");
        }
        Telefone telefone = repositorio.findById(id).orElseThrow(() -> new RuntimeException("Telefone não encontrado"));
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
            return repositorio.save(telefone);
        }
        return telefone;
    }

    public Collection<Telefone> atualizarTodos(Collection<Telefone> atualizacoes) {
        Collection<Telefone> atualizados = new HashSet<>();
        for (Telefone atualizacao : atualizacoes) {
            Telefone telefoneAtualizado = atualizarTelefoneId(atualizacao.getId(), atualizacao);
            atualizados.add(telefoneAtualizado);
        }
        return atualizados;
    }
}