package com.autobots.automanager_usuarios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager_usuarios.modelo.Documento;

@Repository
public interface DocumentoRepositorio extends JpaRepository<Documento, Long> {
}