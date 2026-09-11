package ar.db.projeto_votacao.exception;

public class SessionAlreadyRegistered extends RuntimeException {
    public SessionAlreadyRegistered(String message) {
        super(message);
    }
}
