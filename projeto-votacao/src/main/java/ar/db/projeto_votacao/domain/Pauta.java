package ar.db.projeto_votacao.domain;

import ar.db.projeto_votacao.domain.enums.PautaStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private PautaStatus status = PautaStatus.NOVA;

    @OneToOne
    private Sessao sessao;

    private LocalDateTime criadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "pauta")
    private List<Voto> votos = new ArrayList<>();

    public Pauta(String titulo) {
        this.titulo = titulo;
    }
}
