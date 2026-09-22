package ar.db.projeto_votacao.domain;

import ar.db.projeto_votacao.domain.enums.SessaoStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

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

    //Alterado pra Instant, porque é usado como base no contador regressivo do front,
    //com instant eu não dependo de timezones diferentes pra calcular o contador, o que poderia, e gerou bugs
    @Column(updatable = false, nullable = false)
    private Instant inicio = Instant.now();

    @Column(updatable = false, nullable = false)
    private Instant fim;

    @Enumerated(EnumType.STRING)
    private SessaoStatus status = SessaoStatus.EM_ANDAMENTO;

    public Sessao(Pauta pauta) {
        this.pauta = pauta;
    }
}
