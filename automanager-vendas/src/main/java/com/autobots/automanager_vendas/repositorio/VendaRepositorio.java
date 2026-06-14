package com.autobots.automanager_vendas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager_vendas.modelo.Venda;

@Repository
public interface VendaRepositorio extends JpaRepository<Venda, Long> {
}