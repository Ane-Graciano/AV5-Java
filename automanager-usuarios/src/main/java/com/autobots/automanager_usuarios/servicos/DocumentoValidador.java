package com.autobots.automanager_usuarios.servicos;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.enumeracoes.TipoDocumento;
import com.autobots.automanager_usuarios.modelo.Documento;

@Service
public class DocumentoValidador {
    
    public void documentosDuplicadoMesmoTipo(Collection<Documento> documentos) {
        if (documentos == null) return;
        boolean existeCpf = false;
        boolean existeRg = false;

        for (Documento documento : documentos) {
            TipoDocumento tipo = documento.getTipo();

            if (tipo == TipoDocumento.CPF) {
                if (existeCpf) {
                    throw new RuntimeException("Usuário não pode ter mais de um CPF");
                }
                existeCpf = true;
            }

            if (tipo == TipoDocumento.RG) {
                if (existeRg) {
                    throw new RuntimeException("Usuário não pode ter mais de um RG");
                }
                existeRg = true;
            }
        }
    }
}