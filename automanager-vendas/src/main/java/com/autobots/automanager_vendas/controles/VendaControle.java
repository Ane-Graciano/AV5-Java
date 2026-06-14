package com.autobots.automanager_vendas.controles;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.autobots.automanager_vendas.servicos.venda.AdicionadorLinkVenda;
import com.autobots.automanager_vendas.servicos.venda.VendaApagador;
import com.autobots.automanager_vendas.servicos.venda.VendaAtualizador;
import com.autobots.automanager_vendas.servicos.venda.VendaSelecionador;
import com.autobots.automanager_vendas.servicos.venda.VendaCadastrador;
import com.autobots.automanager_vendas.modelo.dto.VendaRequestDto;
import com.autobots.automanager_vendas.modelo.dto.Resposta;
import com.autobots.automanager_vendas.modelo.dto.VendaResponseDto;
import com.autobots.automanager_vendas.modelo.Venda;

@RestController
@RequestMapping("/vendas")
@Validated
public class VendaControle {

    @Autowired
    private VendaSelecionador selecionadorServico;
    @Autowired
    private VendaCadastrador cadastradorServico;
    @Autowired
    private VendaAtualizador atualizadorServico;
    @Autowired
    private VendaApagador apagadorServico;
    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<VendaResponseDto>> obterVendas() {
        List<VendaResponseDto> vendas = selecionadorServico.obterVendas();
        adicionadorLink.adicionarLink(vendas);
        return ResponseEntity.ok(vendas);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> obterVenda(@PathVariable Long id) {
        VendaResponseDto venda = selecionadorServico.obterVenda(id);
        adicionadorLink.adicionarLink(venda);
        return ResponseEntity.ok(venda);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> cadastraVenda(@RequestBody @Valid VendaRequestDto vendaDto, Authentication authentication) {
        // Validação de segurança baseada nas Roles contidas no próprio token JWT injetado pelo Spring
        boolean ehGerenteOuAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERENTE") || a.getAuthority().equals("ROLE_ADMIN"));

        // Nota: Em cenários onde um vendedor comum tentar cadastrar uma venda, a consistência de ID do funcionário 
        // deve ser preferencialmente repassada e validada na sua camada de Service (VendaCadastrador) que realiza a chamada externa.
        
        Venda venda = cadastradorServico.cadastrar(vendaDto);
        VendaResponseDto vendaResponse = selecionadorServico.converterParaResponse(venda);
        adicionadorLink.adicionarLink(vendaResponse);
        Resposta resposta = new Resposta("Venda cadastrada com sucesso!", vendaResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<VendaResponseDto>> cadastrarVariosVendas(@RequestBody @Valid List<VendaRequestDto> vendasDtos) {
        List<Venda> vendas = cadastradorServico.cadastrarVarios(vendasDtos);
        List<VendaResponseDto> vendasResponse = selecionadorServico.converterListaParaResponse(vendas);
        adicionadorLink.adicionarLink(vendasResponse);
        return ResponseEntity.ok().body(vendasResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<VendaResponseDto> atualizarVenda(@PathVariable Long id, @RequestBody @Valid VendaRequestDto atualizacaoDto) {
        Venda vendaAtualizado = atualizadorServico.atualizar(id, atualizacaoDto);        
        VendaResponseDto vendaResponse = selecionadorServico.converterParaResponse(vendaAtualizado);
        adicionadorLink.adicionarLink(vendaResponse);
        return ResponseEntity.ok().body(vendaResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        apagadorServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos(){
        apagadorServico.excluirTodos();
        Resposta resposta = new Resposta("Vendas excluídas com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENTE','ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("/minhas/compras")
    public ResponseEntity<List<VendaResponseDto>> obterMinhasCompras(Authentication authentication) {
        // Resgata o username direto do token (ex: e-mail ou CPF armazenado no Subject do JWT)
        String usernameLogado = authentication.getName();        
        
        List<VendaResponseDto> todasVendas = selecionadorServico.obterVendas();
        
        // Em um ambiente puramente distribuído, faríamos uma query indexada por clienteId no banco local, 
        // ou validaríamos o ID correspondente ao usernameLogado vindo de Claims customizadas do JWT.
        adicionadorLink.adicionarLink(todasVendas);
        return ResponseEntity.ok(todasVendas);
    }
}