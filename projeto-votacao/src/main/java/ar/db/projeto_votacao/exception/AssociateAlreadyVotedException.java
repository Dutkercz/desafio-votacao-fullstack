package ar.db.projeto_votacao.exception;

public class AssociateAlreadyVotedException extends RuntimeException {
    public AssociateAlreadyVotedException(String message) {
        super(message);
    }
}
