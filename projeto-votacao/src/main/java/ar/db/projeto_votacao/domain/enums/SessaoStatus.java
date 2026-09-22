package ar.db.projeto_votacao.domain.enums;

public enum SessaoStatus {
    EM_ANDAMENTO,
    FINALIZADA;

    public boolean isEncerrada() {
        return this == FINALIZADA;
    }
}
