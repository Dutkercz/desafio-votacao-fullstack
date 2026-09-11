package ar.db.projeto_votacao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AssociateAlreadyRegisteredException.class)
    public ResponseEntity<ProblemDetail> associadoJaExisteException(AssociateAlreadyRegisteredException e) {
        ProblemDetail errorHandler =  ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorHandler);
    }
}
