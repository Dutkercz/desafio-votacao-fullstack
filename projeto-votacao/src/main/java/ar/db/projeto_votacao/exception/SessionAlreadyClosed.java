package ar.db.projeto_votacao.exception;

public class SessionAlreadyClosed extends RuntimeException {
    public SessionAlreadyClosed(String message) {
        super(message);
    }
}
