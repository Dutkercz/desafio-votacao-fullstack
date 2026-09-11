package ar.db.projeto_votacao.repository;

import ar.db.projeto_votacao.domain.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
