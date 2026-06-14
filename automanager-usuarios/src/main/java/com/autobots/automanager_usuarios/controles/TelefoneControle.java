package com.autobots.automanager_usuarios.controles;

import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager_usuarios.modelo.dto.Resposta;
import com.autobots.automanager_usuarios.modelo.Telefone;
import com.autobots.automanager_usuarios.servicos.telefone.TelefoneApagador;
import com.autobots.automanager_usuarios.servicos.telefone.TelefoneAtualizador;
import com.autobots.automanager_usuarios.servicos.telefone.TelefoneCadastrador;
import com.autobots.automanager_usuarios.servicos.telefone.TelefoneSelecionador;
import com.autobots.automanager_usuarios.servicos.telefone.AdicionadorLinkTelefone;

@RestController
@RequestMapping("/telefones")
public class TelefoneControle {

    @Autowired
    private TelefoneCadastrador cadastradorServico;
    @Autowired
    private TelefoneAtualizador atualizadorServico;
    @Autowired
    private TelefoneSelecionador selecionadorServico;
    @Autowired
    private TelefoneApagador apagadorServico;
    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Telefone>> obterTodos() {
        List<Telefone> telefones = selecionadorServico.obterTodos();
        adicionadorLink.adicionarLink(telefones);
        return ResponseEntity.ok().body(telefones);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable Long id) {
        Telefone telefone = selecionadorServico.obterTelefone(id).get();
        adicionadorLink.adicionarLink(telefone);
        return ResponseEntity.ok(telefone);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraTelefone(@RequestBody Telefone telefone) {
        Telefone tel = cadastradorServico.cadastrar(telefone);
        adicionadorLink.adicionarLink(tel);
        Resposta resposta = new Resposta("Telefone cadastrado com sucesso!", tel);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<Telefone>> cadastrarVariosTelefones(@RequestBody List<Telefone> telefones) {
        List<Telefone> tels = cadastradorServico.cadastrarTodos(telefones);
        adicionadorLink.adicionarLink(tels);
        return ResponseEntity.ok().body(tels);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Telefone> atualizarTelefone(@PathVariable Long id, @RequestBody Telefone atualizacao) {
        Telefone telefoneAtualizado = atualizadorServico.atualizarTelefoneId(id, atualizacao);
        adicionadorLink.adicionarLink(telefoneAtualizado);
        return ResponseEntity.ok().body(telefoneAtualizado);
    }
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("")
    public ResponseEntity<Collection<Telefone>> atualizarTodos(@RequestBody Collection<Telefone> atualizacoes) {
        Collection<Telefone> telefonesAtualizados = atualizadorServico.atualizarTodos(atualizacoes);
        telefonesAtualizados.forEach(tel -> adicionadorLink.adicionarLink(tel));
        return ResponseEntity.ok().body(telefonesAtualizados);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Resposta> excluirTelefone(@PathVariable Long id) {
        apagadorServico.excluir(id);
        Resposta resposta = new Resposta("Telefone Excluido com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}