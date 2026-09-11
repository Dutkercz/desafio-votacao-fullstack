package ar.db.projeto_votacao.exception;

public class AssociateAlreadyVoted extends RuntimeException {
    public AssociateAlreadyVoted(String message) {
        super(message);
    }
}
