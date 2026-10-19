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

    @ExceptionHandler(org.springframework.web.method.annotation.HandlerMethodValidationException.class)
    public ResponseEntity<ErroResponse> tratarHandlerMethodValidationException(
            org.springframework.web.method.annotation.HandlerMethodValidationException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Parâmetro inválido",
                "Os parâmetros da requisição não atendem aos critérios de validação");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarMethodArgumentNotValidException(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Dados inválidos",
                "O corpo da requisição contém campos inválidos ou não preenchidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> tratarMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        ErroResponse erro = new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Parâmetro obrigatório ausente",
                String.format("O parâmetro '%s' é obrigatório", ex.getParameterName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> tratarNoResourceFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException ex) {
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
