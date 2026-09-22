package ar.db.projeto_votacao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({AssociateRegisteredException.class, SessionRegisteredException.class})
    public ResponseEntity<ProblemDetail> entidadeExistenteException(RuntimeException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409),
                                                                e.getMessage());
        log.warn("Cadastro duplicado {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(detail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> entidadeNaoEncontradaException(ResourceNotFoundException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404),
                                                                e.getMessage());
        log.warn("Cadastro não encontrado {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    @ExceptionHandler(SessionClosedException.class)
    public ResponseEntity<ProblemDetail> sessaoEncerrada(SessionClosedException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400),
                                                                e.getMessage());
        log.warn("Sessão encerrada {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    }

    @ExceptionHandler(AssociateAlreadyVotedException.class)
    public ResponseEntity<ProblemDetail> sessaoEncerrada(AssociateAlreadyVotedException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                                                                e.getMessage());
        log.warn("Associado tentando registrar voto duplicado {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> badRequestException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());

        e.getFieldErrors().forEach(error -> {
            errors.put("detail", error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
