package com.autobots.automanager_vendas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager_vendas.modelo.Veiculo;

@Repository
public interface VeiculoRepositorio extends JpaRepository<Veiculo, Long> {
}