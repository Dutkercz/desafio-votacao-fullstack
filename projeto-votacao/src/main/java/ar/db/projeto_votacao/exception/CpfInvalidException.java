package ar.db.projeto_votacao.exception;

public class CpfInvalidException extends RuntimeException {
    public CpfInvalidException(String message) {
        super(message);
    }
}
