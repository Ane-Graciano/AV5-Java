package com.autobots.automanager_usuarios.controles;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager_usuarios.modelo.dto.EnderecoDto;
import com.autobots.automanager_usuarios.modelo.dto.Resposta;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.servicos.endereco.AdicionadorLinkEndereco;
import com.autobots.automanager_usuarios.servicos.endereco.EnderecoApagador;
import com.autobots.automanager_usuarios.servicos.endereco.EnderecoAtualizador;
import com.autobots.automanager_usuarios.servicos.endereco.EnderecoCadastrador;
import com.autobots.automanager_usuarios.servicos.endereco.EnderecoSelecionador;

@RestController
@RequestMapping("/enderecos")
public class EnderecoControle {

    @Autowired
    private EnderecoCadastrador cadastradorServico;
    @Autowired
    private EnderecoSelecionador selecionadorServico;
    @Autowired
    private EnderecoAtualizador atualizadorServico;
    @Autowired
    private EnderecoApagador apagadorServico;
    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Endereco>> obteerTodos() {
        List<Endereco> enderecos = selecionadorServico.obterTodos();
        adicionadorLink.adicionarLink(enderecos);
        return ResponseEntity.ok().body(enderecos);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable Long id) {
        Endereco endereco = selecionadorServico.obterEndereco(id).get();
        adicionadorLink.adicionarLink(endereco);
        return ResponseEntity.ok(endereco);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraEndereco(@RequestBody @Valid EnderecoDto enderecoDto) {
        Endereco endereco = cadastradorServico.cadastrar(enderecoDto);
        adicionadorLink.adicionarLink(endereco);
        Resposta resposta = new Resposta("Endereço cadastrado com sucesso!", endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<Endereco>> cadastrarVariosEnderecos(@RequestBody @Valid List<EnderecoDto> enderecosDtos) {
        List<Endereco> enderecos = cadastradorServico.cadastrarTodos(enderecosDtos);
        adicionadorLink.adicionarLink(enderecos);
        return ResponseEntity.ok().body(enderecos);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody @Valid EnderecoDto atualizacaoDto) {
        Endereco enderecoAtualizado = atualizadorServico.atualizarEnderecoId(id, atualizacaoDto);
        adicionadorLink.adicionarLink(enderecoAtualizado);
        return ResponseEntity.ok().body(enderecoAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Resposta> excluirEndereco(@PathVariable Long id) {
        apagadorServico.excluir(id);
        Resposta resposta = new Resposta("Endereço Excluido com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}