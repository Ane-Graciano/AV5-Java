package com.autobots.automanager_vendas.modelo;

import jakarta.persistence.*;
import com.autobots.automanager_vendas.enumeracoes.*;
import lombok.Data;

@Data
@Entity
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeiculo tipo;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String placa;

    @Column(name = "proprietario_id", nullable = false)
    private Long proprietarioId; 
}