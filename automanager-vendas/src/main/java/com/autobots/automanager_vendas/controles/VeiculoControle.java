package com.autobots.automanager_vendas.controles;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import com.autobots.automanager_vendas.servicos.veiculo.AdicionadorLinkVeiculo;
import com.autobots.automanager_vendas.servicos.veiculo.VeiculoApagador;
import com.autobots.automanager_vendas.servicos.veiculo.VeiculoAtualizador;
import com.autobots.automanager_vendas.servicos.veiculo.VeiculoSelecionador;
import com.autobots.automanager_vendas.servicos.veiculo.VeiculoCadastrador;
import com.autobots.automanager_vendas.modelo.dto.VeiculoDto;
import com.autobots.automanager_vendas.modelo.dto.Resposta;
import com.autobots.automanager_vendas.modelo.dto.VeiculoResponseDto;
import com.autobots.automanager_vendas.modelo.Veiculo;

@RestController
@RequestMapping("/veiculos")
@Validated
public class VeiculoControle {

    @Autowired
    private VeiculoSelecionador selecionadorServico;
    @Autowired
    private VeiculoCadastrador cadastradorServico;
    @Autowired
    private VeiculoAtualizador atualizadorServico;
    @Autowired
    private VeiculoApagador apagadorServico;
    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<VeiculoResponseDto>> obterVeiculos() {
        List<Veiculo> veiculos = selecionadorServico.obterVeiculos();
        List<VeiculoResponseDto> response = selecionadorServico.converterListaParaResponse(veiculos);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDto> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = selecionadorServico.obterVeiculo(id);
        VeiculoResponseDto response = selecionadorServico.converterParaResponse(veiculo);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraVeiculo(@RequestBody @Valid VeiculoDto veiculoDto) {
        Veiculo veiculo = cadastradorServico.cadastrar(veiculoDto);        
        Veiculo veiculoCompleto = selecionadorServico.obterVeiculo(veiculo.getId());
        VeiculoResponseDto response = selecionadorServico.converterParaResponse(veiculoCompleto);
        adicionadorLink.adicionarLink(response);
        Resposta resposta = new Resposta("Veículo cadastrado com sucesso!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<VeiculoResponseDto>> cadastrarVariosVeiculos(@RequestBody @Valid List<VeiculoDto> veiculosDtos) {
        List<Veiculo> veiculos = cadastradorServico.cadastrarVarios(veiculosDtos);
        List<VeiculoResponseDto> response = selecionadorServico.converterListaParaResponse(veiculos);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<VeiculoResponseDto> atualizarVeiculo(@PathVariable Long id, @RequestBody @Valid VeiculoDto atualizacaoDto) {
        Veiculo veiculoAtualizado = atualizadorServico.atualizar(id, atualizacaoDto);
        VeiculoResponseDto response = selecionadorServico.converterParaResponse(veiculoAtualizado);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        apagadorServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos() {
        apagadorServico.excluirTodos();
        Resposta resposta = new Resposta("Veículos excluídos com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}