package ar.db.projeto_votacao.domain;

import jakarta.persistence.*;
import lombok.*;

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

    public Sessao(Pauta pauta) {
        this.pauta = pauta;
    }

}
