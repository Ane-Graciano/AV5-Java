package com.autobots.automanager_usuarios.controles;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager_usuarios.modelo.dto.UsuarioDto;
import com.autobots.automanager_usuarios.modelo.dto.Resposta;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.servicos.usuario.AdicionadorLinkUsuario;
import com.autobots.automanager_usuarios.servicos.usuario.UsuarioApagador;
import com.autobots.automanager_usuarios.servicos.usuario.UsuarioAtualizador;
import com.autobots.automanager_usuarios.servicos.usuario.UsuarioSelecionador;
import com.autobots.automanager_usuarios.servicos.usuario.UsuarioCadastrador;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioControle {

    @Autowired
    private UsuarioSelecionador selecionadorServico;
    @Autowired
    private UsuarioCadastrador cadastradorServico;
    @Autowired
    private UsuarioAtualizador atualizadorServico;
    @Autowired
    private UsuarioApagador apagadorServico;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @GetMapping("")
    public ResponseEntity<List<Usuario>> obterUsuarios(Authentication authentication) {
        String usuarioLogado = authentication.getName();
        List<Usuario> usuarios = selecionadorServico.obterUsuarios(usuarioLogado);
        adicionadorLink.adicionarLink(usuarios);
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id, Authentication authentication) {
        String usuarioLogado = authentication.getName();
        Usuario usuario = selecionadorServico.obterUsuario(id, usuarioLogado);
        adicionadorLink.adicionarLink(usuario);
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @PostMapping("")
    public ResponseEntity<Resposta> cadastraUsuario(@RequestBody @Valid UsuarioDto usuarioDto, Authentication authentication) {
        String usuarioLogado = authentication.getName();
        // Ajustado para o padrão correto do seu cadastrador estruturado
        Usuario usuario = cadastradorServico.cadastrar(usuarioDto, usuarioLogado, usuarioDto.getIdEmpresa());
        adicionadorLink.adicionarLink(usuario);
        Resposta resposta = new Resposta("Usuário cadastrado com sucesso!", usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @PostMapping("/varios")
    public ResponseEntity<List<Usuario>> cadastrarVariosUsuarios(@RequestBody @Valid List<UsuarioDto> usuariosDtos, Authentication authentication) {
        String usuarioLogado = authentication.getName();
        List<Usuario> usuarios = cadastradorServico.cadastrarVarios(usuariosDtos, usuarioLogado);
        adicionadorLink.adicionarLink(usuarios);
        return ResponseEntity.ok().body(usuarios);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioDto atualizacaoDto, Authentication authentication) {
        String usuarioLogado = authentication.getName();
        Usuario usuarioAtualizado = atualizadorServico.atualizar(id, atualizacaoDto, usuarioLogado);
        adicionadorLink.adicionarLink(usuarioAtualizado);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id, Authentication authentication) {
        String usuarioLogado = authentication.getName();
        apagadorServico.excluir(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GERENTE')")
    @DeleteMapping("")
    public ResponseEntity<Resposta> excluirTodos(Authentication authentication){
        String usuarioLogado = authentication.getName();
        apagadorServico.excluirTodos(usuarioLogado);
        Resposta resposta = new Resposta("Usuários Excluidos com sucesso", null);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENTE','ROLE_VENDEDOR','ROLE_ADMIN','ROLE_GERENTE')")
    @GetMapping("/pessoal")
    public ResponseEntity<Usuario> obterInformacaoPessoal(Authentication authentication) {
        String usernameLogado = authentication.getName();
        Usuario usuario = selecionadorServico.obterPorNome(usernameLogado);
        adicionadorLink.adicionarLink(usuario);
        return ResponseEntity.ok(usuario);
    }
}