package com.autobots.automanager_usuarios.modelo;

import java.util.Date;

import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager_usuarios.enumeracoes.TipoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
public class Documento extends RepresentationModel<Documento> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TipoDocumento tipo;
    
    @Column(nullable = false)
    private Date dataEmissao;
    
    @Column(unique = true, nullable = false)
    private String numero;

    public void setNumero(String numero) {
        if (numero != null) {
            this.numero = numero.replaceAll("\\D", "");
        } else {
            this.numero = null;
        }
    }

    @AssertTrue(message = "Número do documento inválido para o tipo informado")
    public boolean isNumeroValido() {
        if (tipo == null || numero == null) {
            return false;
        }

        String numeroLimpo = numero.replaceAll("\\D", "");

        if (tipo == TipoDocumento.CPF) {
            if (numeroLimpo.length() != 11) return false;
            CPFValidator cpfValidador = new CPFValidator();
            cpfValidador.initialize(null);
            return cpfValidador.isValid(numeroLimpo, null);
        }

        if (tipo == TipoDocumento.CNPJ) {
            if (numeroLimpo.length() != 14) return false;
            CNPJValidator cnpjValidador = new CNPJValidator();
            cnpjValidador.initialize(null);
            return cnpjValidador.isValid(numeroLimpo, null);
        }

        if (tipo == TipoDocumento.RG) {
            String numeroRg = numero.toUpperCase().replaceAll("[^0-9X]", "");
            return numeroRg.matches("^[0-9]{7,9}[0-9X]{1}$");
        }

        return true;
    }
}