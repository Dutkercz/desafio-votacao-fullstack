package ar.db.projeto_votacao.repository;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

    ///Retona o resultado páginado, ordenado de Pautas,
    /// primeiro as com sessão em andamento. para aparecerem nas primeiras páginas
    /// depois as que não possuem sessão inicializada e as finalizada não entram nesse retorno
    @Query("""
           SELECT P FROM Pauta P
           LEFT JOIN P.sessao s
           WHERE P.status <> PautaStatus.FINALIZADA
           ORDER BY
           CASE
                WHEN s.status = ar.db.projeto_votacao.domain.enums.SessaoStatus.EM_ANDAMENTO then 1
                WHEN s.status IS null then 2
                ELSE 3 END,
           P.criadoEm DESC
           """)
    Page<Pauta> findNaoFinalizadosOrdenados(Pageable pageable);

    ///Busca as pautas baseadas em PautaStatus recebida por parametro,
    /// no projeto é usada pra trazer as pautas finalizadas de forma paginada
    @Query("""
           SELECT P FROM Pauta P
           WHERE P.status = :pautaStatus
           ORDER BY
           P.criadoEm DESC
           """)
    Page<Pauta> findPorStatus(@Param("pautaStatus") PautaStatus pautaStatus, Pageable pageable);
}
