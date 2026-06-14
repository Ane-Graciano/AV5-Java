package com.autobots.automanager_vendas.servicos.venda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.autobots.automanager_vendas.modelo.Veiculo;
import com.autobots.automanager_vendas.modelo.Venda;
import com.autobots.automanager_vendas.modelo.dto.VendaRequestDto; 
import com.autobots.automanager_vendas.repositorio.VeiculoRepositorio;
import com.autobots.automanager_vendas.repositorio.VendaRepositorio;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VendaCadastrador {

    @Autowired
    private VendaRepositorio repositorio;

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private RestTemplate restTemplate;

    private final String URL_USUARIOS = "http://localhost:8081";
    private final String URL_CATALOGO = "http://localhost:8082"; 
    
    @Transactional
    public Venda cadastrar(VendaRequestDto vendaDto) {
        Venda venda = new Venda();
        venda.setCadastro(new Date());
        venda.setIdentificacao(vendaDto.getIdentificacao());

        Veiculo veiculo = veiculoRepositorio.findById(vendaDto.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado localmente na base de Vendas"));
        venda.setVeiculo(veiculo);

        HttpEntity<Void> entitySeguranca = extrairTokenContexto();

        validarEntidadeExterna(URL_USUARIOS + "/empresas/" + vendaDto.getIdEmpresa(), entitySeguranca, "Empresa informada inválida ou inexistente.");
        validarEntidadeExterna(URL_USUARIOS + "/usuarios/" + vendaDto.getClienteId(), entitySeguranca, "Cliente informado inválido ou inexistente.");
        validarEntidadeExterna(URL_USUARIOS + "/usuarios/" + vendaDto.getFuncionarioId(), entitySeguranca, "Funcionário informado inválido ou inexistente.");

        venda.setIdEmpresa(vendaDto.getIdEmpresa());
        venda.setClienteId(vendaDto.getClienteId());
        venda.setFuncionarioId(vendaDto.getFuncionarioId());

        if (vendaDto.getMercadoriasIds() != null) {
            for (Long mercadoriaId : vendaDto.getMercadoriasIds()) {
                validarEntidadeExterna(URL_CATALOGO + "/mercadorias/" + mercadoriaId, entitySeguranca, "A mercadoria com ID " + mercadoriaId + " não existe.");
                venda.getMercadoriasIds().add(mercadoriaId);
            }
        }

        if (vendaDto.getServicosIds() != null) {
            for (Long servicoId : vendaDto.getServicosIds()) {
                validarEntidadeExterna(URL_CATALOGO + "/servicos/" + servicoId, entitySeguranca, "O serviço com ID " + servicoId + " não existe.");
                venda.getServicosIds().add(servicoId);
            }
        }

        return repositorio.save(venda);
    }

    public List<Venda> cadastrarVarios(List<VendaRequestDto> vendasDtos) {
        List<Venda> vendas = new ArrayList<>();
        for (VendaRequestDto dto : vendasDtos) {
            vendas.add(cadastrar(dto));
        }
        return vendas;
    }

    private HttpEntity<Void> extrairTokenContexto() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpHeaders headers = new HttpHeaders();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("Authorization");
            if (token != null) {
                headers.set("Authorization", token);
            }
        }
        return new HttpEntity<>(headers);
    }

    private void validarEntidadeExterna(String url, HttpEntity<Void> entity, String mensagemErro) {
        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Falha na validação entre serviços: " + mensagemErro);
        }
    }
}