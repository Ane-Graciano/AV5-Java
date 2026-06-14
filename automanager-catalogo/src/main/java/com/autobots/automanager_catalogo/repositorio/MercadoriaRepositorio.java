package com.autobots.automanager_catalogo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager_catalogo.modelo.Mercadoria;

public interface MercadoriaRepositorio extends JpaRepository<Mercadoria, Long> {
}