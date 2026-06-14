package com.autobots.automanager_usuarios.servicos;

import java.util.Date;

import com.autobots.automanager_usuarios.enumeracoes.TipoDocumento;
import com.autobots.automanager_usuarios.modelo.Usuario;

public class VerificadorNulo {

    public boolean verificar(TipoDocumento dado) {
        return dado == null;
    }

    public boolean verificar(String dado) {
        return dado == null || dado.isBlank();
    }

    public boolean verificar(Date dado) {
        return dado == null;
    }

    public boolean verificar(Long dado) {
        return dado == null;
    }

    public boolean verificar(Double dado) {
        return dado == null;
    }

    public boolean verificar(Usuario dado) {
        return dado == null;
    }
}
