package ar.db.projeto_votacao.domain;

import ar.db.projeto_votacao.domain.enums.SessaoStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Pauta pauta;

    @Column(updatable = false, nullable = false)
    private LocalDateTime inicio = LocalDateTime.now();

    @Column(updatable = false, nullable = false)
    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    private SessaoStatus status = SessaoStatus.EM_ANDAMENTO;

    public Sessao(Pauta pauta) {
        this.pauta = pauta;
    }
}
