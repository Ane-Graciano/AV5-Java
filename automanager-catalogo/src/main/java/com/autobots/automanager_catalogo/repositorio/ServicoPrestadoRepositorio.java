package com.autobots.automanager_catalogo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager_catalogo.modelo.ServicoPrestado;

public interface ServicoPrestadoRepositorio extends JpaRepository<ServicoPrestado, Long> {
}