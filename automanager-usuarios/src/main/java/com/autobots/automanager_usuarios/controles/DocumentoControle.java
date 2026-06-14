package com.autobots.automanager_usuarios.controles;

import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager_usuarios.modelo.dto.Resposta;
import com.autobots.automanager_usuarios.modelo.Documento;
import com.autobots.automanager_usuarios.servicos.documento.AdicionadorLinkDocumento;
import com.autobots.automanager_usuarios.servicos.documento.DocumentoApagador;
import com.autobots.automanager_usuarios.servicos.documento.DocumentoAtualizador;
import com.autobots.automanager_usuarios.servicos.documento.DocumentoCadastrador;
import com.autobots.automanager_usuarios.servicos.documento.DocumentoSelecionador;

@RestController
@RequestMapping("/documentos")
public class DocumentoControle {

    @Autowired
    private DocumentoCadastrador cadastradorServico;
    @Autowired
    private DocumentoSelecionador selecionadorServico;
    @Autowired
    private DocumentoAtualizador atualizadorServico;
    @Autowired
    private DocumentoApagador apagadorServico;
    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Documento>> obterTodos() {
        List<Documento> documentos = selecionadorServico.obterTodos();
        adicionadorLink.adicionarLink(documentos);
        return ResponseEntity.ok().body(documentos);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obeterDocumento(@PathVariable Long id) {
        Documento documento = selecionadorServico.obeterDocumento(id).get();
        adicionadorLink.adicionarLink(documento);
        return ResponseEntity.ok(documento);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraDocumento(@RequestBody Documento documento) {
        Documento doc = cadastradorServico.cadastrar(documento);
        adicionadorLink.adicionarLink(doc);
        Resposta resposta = new Resposta("Documento cadastrado com sucesso!", doc);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/varios")
    public ResponseEntity<List<Documento>> cadastrarVariosDocumentos(@RequestBody List<Documento> documentos) {
        List<Documento> docs = cadastradorServico.cadastrarTodos(documentos);
        adicionadorLink.adicionarLink(docs);
        return ResponseEntity.ok().body(docs);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Documento> atualizarDocumento(@PathVariable Long id, @RequestBody Documento atualizacao) {
        Documento documentoAtualizado = atualizadorServico.atualizarDocumentoId(id, atualizacao);
        adicionadorLink.adicionarLink(documentoAtualizado);
        return ResponseEntity.ok().body(documentoAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("")
    public ResponseEntity<Collection<Documento>> atualizarTodos(@RequestBody Collection<Documento> atualizacoes) {
        Collection<Documento> documentosAtualizados = atualizadorServico.atualizarTodos(atualizacoes);
        documentosAtualizados.forEach(doc -> adicionadorLink.adicionarLink(doc));
        return ResponseEntity.ok().body(documentosAtualizados);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Resposta> excluirDocumento(@PathVariable Long id) {
        apagadorServico.excluir(id);
        Resposta resposta = new Resposta("Documento Excluido com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}