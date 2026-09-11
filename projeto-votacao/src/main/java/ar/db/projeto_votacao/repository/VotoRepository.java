package ar.db.projeto_votacao.repository;

import ar.db.projeto_votacao.domain.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
}
