package com.autobots.automanager_vendas.modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date cadastro;

    @Column(nullable = false, unique = true)
    private String identificacao;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId; 

    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId; 

    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa; 

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo; 
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "venda_mercadorias", joinColumns = @JoinColumn(name = "venda_id"))
    @Column(name = "mercadoria_id")
    private Set<Long> mercadoriasIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "venda_servicos", joinColumns = @JoinColumn(name = "venda_id"))
    @Column(name = "servico_id")
    private Set<Long> servicosIds = new HashSet<>();
}