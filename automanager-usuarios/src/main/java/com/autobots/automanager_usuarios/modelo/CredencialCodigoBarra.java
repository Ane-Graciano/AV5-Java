package com.autobots.automanager_usuarios.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class CredencialCodigoBarra extends Credencial {
    @Column(nullable = false, unique = true)
    private long codigo;
}