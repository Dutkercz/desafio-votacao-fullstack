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
    private LocalDateTime inicio;

    private LocalDateTime fim;

    public Sessao(Pauta pauta, Integer endingMinutes) {
        this.pauta = pauta;
        this.inicio = LocalDateTime.now();
        this.fim = inicio.plusMinutes(endingMinutes);
    }
}
