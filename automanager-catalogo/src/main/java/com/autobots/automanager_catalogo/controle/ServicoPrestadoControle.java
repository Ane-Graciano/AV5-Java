package com.autobots.automanager_catalogo.controle;

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

import com.autobots.automanager_catalogo.modelo.ServicoPrestado;
import com.autobots.automanager_catalogo.modelo.dto.Resposta;
import com.autobots.automanager_catalogo.servico.servicoPrestado.AdicionadorLinkServicoPrestado;
import com.autobots.automanager_catalogo.servico.servicoPrestado.ServicoPrestadoApagador;
import com.autobots.automanager_catalogo.servico.servicoPrestado.ServicoPrestadoAtualizador;
import com.autobots.automanager_catalogo.servico.servicoPrestado.ServicoPrestadoCadastrador;
import com.autobots.automanager_catalogo.servico.servicoPrestado.ServicoPrestadoSelecionador;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/servicos")
@Validated
public class ServicoPrestadoControle {
    
    @Autowired
    private ServicoPrestadoCadastrador cadastradorServico;
    @Autowired
    private ServicoPrestadoSelecionador selecionadorServico;
    @Autowired
    private ServicoPrestadoAtualizador atualizadorServico;
    @Autowired 
    private ServicoPrestadoApagador apagadorServico;
    @Autowired
    private AdicionadorLinkServicoPrestado adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<ServicoPrestado>> obterServicosPrestados(){
        List<ServicoPrestado> servicoPrestados = selecionadorServico.obterServicosPrestados();
        adicionadorLink.adicionarLink(servicoPrestados);
        return ResponseEntity.ok(servicoPrestados);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_GERENTE','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoPrestado> obterServicoPrestado(@PathVariable Long id) {
        ServicoPrestado servicoPrestado = selecionadorServico.obterServicoPrestado(id).get();
        adicionadorLink.adicionarLink(servicoPrestado);
        return ResponseEntity.ok(servicoPrestado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraServicoPrestado(@RequestBody @Valid ServicoPrestado servicoPrestado) {
        ServicoPrestado servicoPrestadoCadastrado = cadastradorServico.cadastrar(servicoPrestado);
        adicionadorLink.adicionarLink(servicoPrestadoCadastrado);
        Resposta resposta = new Resposta("Servico cadastrado com sucesso!", servicoPrestadoCadastrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<ServicoPrestado>> cadastrarVariosSevicosPrestados(@RequestBody @Valid List<ServicoPrestado> servicosPrestados) {
        List<ServicoPrestado> servicosPrestadosCadastrados = cadastradorServico.cadastrarVarios(servicosPrestados);
        adicionadorLink.adicionarLink(servicosPrestadosCadastrados);
        return ResponseEntity.ok().body(servicosPrestadosCadastrados);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ServicoPrestado> atualizarServicoPrestado(@PathVariable Long id, @RequestBody @Valid ServicoPrestado servicoPrestado) {
        ServicoPrestado ServicoPrestadoAtualizado = atualizadorServico.atualizar(id, servicoPrestado);
        adicionadorLink.adicionarLink(ServicoPrestadoAtualizado);
        return ResponseEntity.ok().body(ServicoPrestadoAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @PatchMapping("")
    public ResponseEntity<List<ServicoPrestado>> atualizarTodos(@RequestBody @Valid List<ServicoPrestado> atualizacoes){
        List<ServicoPrestado> servicosPrestadosAtualizados = atualizadorServico.atualizarTodos(atualizacoes);
        servicosPrestadosAtualizados.forEach(servicoPrestadoAtualizado -> adicionadorLink.adicionarLink(servicoPrestadoAtualizado));
        return ResponseEntity.ok().body(servicosPrestadosAtualizados);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirServicoPrestado(@PathVariable Long id) {
        apagadorServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE','ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos(){
        apagadorServico.excluirTodos();
        Resposta resposta = new Resposta("Servicos Excluidos com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}