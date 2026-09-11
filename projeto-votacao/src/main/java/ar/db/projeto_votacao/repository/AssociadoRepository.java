package ar.db.projeto_votacao.repository;

import ar.db.projeto_votacao.domain.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
    boolean existsByCpf(String cpf);
}
