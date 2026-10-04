package investfacil.demo.shared.adapter.in.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import investfacil.demo.shared.domain.exception.DadoInvalidoException;
import investfacil.demo.shared.domain.exception.RecursoJaExisteException;
import investfacil.demo.shared.domain.exception.RegraDeNegocioException;

/**
 * Traduz exceções em respostas HTTP no formato Problem Details (RFC 9457).
 * Todos os erros de campo saem em "erros": { "campo": "mensagem" },
 * venham do Bean Validation (DTO) ou do domínio.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        problema.setProperty("erros", erros);
        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido ou com formato incorreto");
    }

    @ExceptionHandler(DadoInvalidoException.class)
    ProblemDetail tratarDadoInvalido(DadoInvalidoException ex) {
        return comCampo(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RecursoJaExisteException.class)
    ProblemDetail tratarRecursoJaExiste(RecursoJaExisteException ex) {
        return comCampo(HttpStatus.CONFLICT, ex);
    }

    // Duas requisições simultâneas podem passar pela verificação de duplicidade; a constraint UNIQUE do banco barra a segunda
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail tratarViolacaoDeIntegridade(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Registro já existe ou viola uma restrição");
    }

    private static ProblemDetail comCampo(HttpStatus status, RegraDeNegocioException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        if (ex.getCampo() != null) {
            problema.setProperty("erros", Map.of(ex.getCampo(), ex.getMessage()));
        }
        return problema;
    }
}
