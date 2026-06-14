package com.autobots.automanager_usuarios.modelo;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "tipo"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CredencialUsuarioSenha.class, name = "usuarioSenha"),
  @JsonSubTypes.Type(value = CredencialCodigoBarra.class, name = "codigoBarra")
})
public abstract class Credencial extends RepresentationModel<Credencial> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date criacao;

    @Column
    private Date ultimoAcesso;

    @Column(nullable = false)
    private boolean inativo;
}