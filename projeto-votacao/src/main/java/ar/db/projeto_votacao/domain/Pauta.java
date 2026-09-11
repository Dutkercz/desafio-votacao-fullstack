package ar.db.projeto_votacao.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne
    private Sessao sessao;

    @OneToMany(mappedBy = "pauta")
    private List<Voto> votos = new ArrayList<>();

    public Pauta(String titulo) {
        this.titulo = titulo;
    }
}
