package ar.db.projeto_votacao.exception;

public class SessionClosedException extends RuntimeException {
    public SessionClosedException(String message) {
        super(message);
    }
}
