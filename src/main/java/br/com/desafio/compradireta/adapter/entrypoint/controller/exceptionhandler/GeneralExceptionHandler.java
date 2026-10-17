package br.com.desafio.compradireta.adapter.entrypoint.controller.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroResponse> tratarNegocioException(NegocioException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Regra de negócio violada",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarExceptionGenerica(Exception ex) {
        LOGGER.error("Erro inesperado ao processar a solicitação", ex);
        ErroResponse erro = new ErroResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno",
                "Ocorreu um erro inesperado ao processar a solicitação");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
