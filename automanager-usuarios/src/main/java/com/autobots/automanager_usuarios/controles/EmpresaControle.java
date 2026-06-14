package com.autobots.automanager_usuarios.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager_usuarios.modelo.Empresa;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaRequestDto;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaResponseDto;
import com.autobots.automanager_usuarios.modelo.dto.Resposta;
import com.autobots.automanager_usuarios.servicos.empresa.AdicionadorLinkEmpresa;
import com.autobots.automanager_usuarios.servicos.empresa.EmpresaApagador;
import com.autobots.automanager_usuarios.servicos.empresa.EmpresaAtualizadora;
import com.autobots.automanager_usuarios.servicos.empresa.EmpresaCadastradora;
import com.autobots.automanager_usuarios.servicos.empresa.EmpresaSelecionador;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresas")
@Validated
public class EmpresaControle {

    @Autowired
    private EmpresaSelecionador selecionadorServico;
    @Autowired
    private EmpresaCadastradora cadastradorServico;
    @Autowired
    private EmpresaAtualizadora atualizadorServico;
    @Autowired
    private EmpresaApagador apagadorServico;
    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<EmpresaResponseDto>> obterEmpresas() {
        List<Empresa> empresas = selecionadorServico.obterEmpresas();
        List<EmpresaResponseDto> response = selecionadorServico.converterListaParaResponse(empresas);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')") // Liberado para as consultas internas via RestTemplate
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDto> obterEmpresa(@PathVariable Long id) {
        Empresa empresa = selecionadorServico.obtenerEmpresa(id);
        EmpresaResponseDto response = selecionadorServico.converterParaResponse(empresa);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraEmpresa(@RequestBody @Valid EmpresaRequestDto empresaDto) {
        Empresa empresa = cadastradorServico.cadastrar(empresaDto);
        Empresa empresaCompleta = selecionadorServico.obtenerEmpresa(empresa.getId());
        EmpresaResponseDto response = selecionadorServico.converterParaResponse(empresaCompleta);
        adicionadorLink.adicionarLink(response);
        Resposta resposta = new Resposta("Empresa cadastrada com sucesso!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<EmpresaResponseDto>> cadastrarVariasEmpresas(@RequestBody @Valid List<EmpresaRequestDto> empresasDtos) {
        List<Empresa> empresas = cadastradorServico.cadastrarVarios(empresasDtos);
        List<EmpresaResponseDto> response = selecionadorServico.converterListaParaResponse(empresas);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<EmpresaResponseDto> atualizarEmpresa(@PathVariable Long id, @RequestBody @Valid EmpresaRequestDto atualizacaoDto) {
        Empresa empresaAtualizada = atualizadorServico.atualizar(id, atualizacaoDto);
        EmpresaResponseDto response = selecionadorServico.converterParaResponse(empresaAtualizada);
        adicionadorLink.adicionarLink(response);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        apagadorServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos(){
        apagadorServico.excluirTodos();
        Resposta resposta = new Resposta("Empresas Excluidas com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}