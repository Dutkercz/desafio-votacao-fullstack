package ar.db.projeto_votacao.repository;

import ar.db.projeto_votacao.domain.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    boolean existsByPautaId(Long pautaId);
}
