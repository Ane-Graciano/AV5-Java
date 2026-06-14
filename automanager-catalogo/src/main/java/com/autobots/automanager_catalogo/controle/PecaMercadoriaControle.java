package com.autobots.automanager_catalogo.controle;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager_catalogo.modelo.dto.Resposta;
import com.autobots.automanager_catalogo.modelo.Mercadoria;
import com.autobots.automanager_catalogo.servico.pecaMercadoria.AdicionadorLinkPecaMercadoria;
import com.autobots.automanager_catalogo.servico.pecaMercadoria.PecaMercadoriaApagador;
import com.autobots.automanager_catalogo.servico.pecaMercadoria.PecaMercadoriaAtualizador;
import com.autobots.automanager_catalogo.servico.pecaMercadoria.PecaMercadoriaCadastrador;
import com.autobots.automanager_catalogo.servico.pecaMercadoria.PecaMercadoriaSelecionador;

@RestController
@RequestMapping("/mercadorias")
@Validated
public class PecaMercadoriaControle {
    
    @Autowired
    private PecaMercadoriaCadastrador cadastradorServico;
    @Autowired
    private PecaMercadoriaSelecionador selecionadorServico;
    @Autowired
    private PecaMercadoriaAtualizador atualizadorServico;
    @Autowired 
    private PecaMercadoriaApagador apagadorServico;
    @Autowired
    private AdicionadorLinkPecaMercadoria adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Mercadoria>> obterMercadorias(){
        List<Mercadoria> mercadorias = selecionadorServico.obterMercadorias();
        adicionadorLink.adicionarLink(mercadorias);
        return ResponseEntity.ok(mercadorias);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = selecionadorServico.obterMercadoria(id).get();
        adicionadorLink.adicionarLink(mercadoria);
        return ResponseEntity.ok(mercadoria);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraMercadoria(@RequestBody @Valid Mercadoria mercadoria) {
        Mercadoria mercadoriaCadastrada = cadastradorServico.cadastrar(mercadoria);
        adicionadorLink.adicionarLink(mercadoriaCadastrada);
        Resposta resposta = new Resposta("Mercadoria cadastrada com sucesso!", mercadoriaCadastrada);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<Mercadoria>> cadastrarVariasMercadorias(@RequestBody @Valid List<Mercadoria> mercadorias) {
        List<Mercadoria> mercadoriasCadastradas = cadastradorServico.cadastrarVarios(mercadorias);
        adicionadorLink.adicionarLink(mercadoriasCadastradas);
        return ResponseEntity.ok().body(mercadoriasCadastradas);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Mercadoria> atualizarMercadoria(@PathVariable Long id, @RequestBody @Valid Mercadoria mercadoria) {
        Mercadoria mercadoriaAtualizado = atualizadorServico.atualizar(id, mercadoria);
        adicionadorLink.adicionarLink(mercadoriaAtualizado);
        return ResponseEntity.ok().body(mercadoriaAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PatchMapping("")
    public ResponseEntity<List<Mercadoria>> atualizarTodos(@RequestBody @Valid List<Mercadoria> atualizacoes){
        List<Mercadoria> mercadoriasAtualizadas = atualizadorServico.atualizarTodos(atualizacoes);
        mercadoriasAtualizadas.forEach(mercadoriasAtualizada -> adicionadorLink.adicionarLink(mercadoriasAtualizada));
        return ResponseEntity.ok().body(mercadoriasAtualizadas);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        apagadorServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos(){
        apagadorServico.excluirTodos();
        Resposta resposta = new Resposta("Mercadorias Excluidas com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}