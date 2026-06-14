package com.autobots.automanager_usuarios.excecao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.autobots.automanager_usuarios.excecao.personalizado.CadastroDuplicado;
import com.autobots.automanager_usuarios.excecao.personalizado.UsuarioNaoExiste;
import com.autobots.automanager_usuarios.modelo.dto.ErroRespostaDto;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ManipuladorGlobal {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDto> manipularValidacao(MethodArgumentNotValidException ex) {
        List<String> mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        ErroRespostaDto erroResposta = new ErroRespostaDto("Erro de validação", String.join(", ", mensagem));
        return new ResponseEntity<>(erroResposta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroRespostaDto> manipularViolacaoIntegridade(DataIntegrityViolationException ex) {
        String mensagemGeral = "Verifique os dados enviados";
        Throwable causa = ex.getCause();
        while (causa != null) {
            String mensagem = causa.getMessage().toLowerCase();
            if (mensagem.contains("numero")) {
                mensagemGeral = "Já existe um documento com esse número Moço, Tente colocar outro";
                break;
            }
            if (mensagem.contains("telefones_id") || mensagem.contains("telefone_id")) {
                mensagemGeral = "Esse telefone está sendo usado por um usuário ativo e não pode ser excluído.";
                break;
            }
            if (mensagem.contains("endereco_id")) {
                mensagemGeral = "Esse endereço está vinculado a um usuário e não pode ser alterado diretamente.";
                break;
            }
            if (mensagem.contains("documentos_id")) {
                mensagemGeral = "Esse documento está vinculado a um usuário e não pode ser removido.";
                break;
            }
            causa = causa.getCause();
        }

        ErroRespostaDto erroResposta = new ErroRespostaDto("Violação de Integridade", mensagemGeral);
        return new ResponseEntity<>(erroResposta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CadastroDuplicado.class)
    public ResponseEntity<ErroRespostaDto> manipularCadastroDuplicado(CadastroDuplicado ex) {
        ErroRespostaDto erroResposta = new ErroRespostaDto(ex.getMessage(), ex.getMensagem());
        return new ResponseEntity<>(erroResposta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioNaoExiste.class)
    public ResponseEntity<ErroRespostaDto> manipularUsuarioNaoExiste(UsuarioNaoExiste ex) {
        ErroRespostaDto erroResposta = new ErroRespostaDto(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResposta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroRespostaDto> manipularConstraintViolation(ConstraintViolationException ex) {
        String mensagens = ex.getConstraintViolations()
                .stream()
                .map(violacao -> violacao.getMessage())
                .collect(Collectors.joining(", "));

        ErroRespostaDto erroResposta = new ErroRespostaDto("Erro de validação de dados", mensagens);
        return new ResponseEntity<>(erroResposta, HttpStatus.BAD_REQUEST);
    }
}
