package com.autobots.automanager_usuarios.servicos.documento;

import java.util.Collection;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Documento;
import com.autobots.automanager_usuarios.repositorios.DocumentoRepositorio;
import com.autobots.automanager_usuarios.servicos.VerificadorNulo;

@Service
public class DocumentoAtualizador {
    @Autowired
    private DocumentoRepositorio repositorio;

    private VerificadorNulo verificador = new VerificadorNulo();

    public void atualizar(Documento documento, Documento atualizacao) {
        if (documento != null && atualizacao != null) {
            if (!verificador.verificar(atualizacao.getTipo())) {
                documento.setTipo(atualizacao.getTipo());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                documento.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(Collection<Documento> documentos, Collection<Documento> atualizacoes) {
        if (documentos == null || atualizacoes == null) return;
        for (Documento atualizacao : atualizacoes) {
            for (Documento documento : documentos) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(documento.getId())) {
                    atualizar(documento, atualizacao);
                }
            }
        }
    }

    public Documento atualizarDocumentoId(Long id, Documento atualizacao) {
        if(!repositorio.existsById(id)){
            throw new RuntimeException("O documento de ID " + id + " não foi encontrado no sistema.");
        }
        Documento documento = repositorio.findById(id).orElseThrow(() -> new RuntimeException("Documento não encontrado"));
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getNumero())) {
                documento.setNumero(atualizacao.getNumero());
            }
            if (!verificador.verificar(atualizacao.getTipo())) {
                documento.setTipo(atualizacao.getTipo());
            }
            return repositorio.save(documento);
        }
        return documento;
    }

    public Collection<Documento> atualizarTodos(Collection<Documento> atualizacoes) {
        Collection<Documento> atualizados = new HashSet<>();
        for (Documento atualizacao : atualizacoes) {
            Documento documentoAtualizado = atualizarDocumentoId(atualizacao.getId(), atualizacao);
            atualizados.add(documentoAtualizado);
        }
        return atualizados;
    }
}