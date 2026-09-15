package ar.db.projeto_votacao.domain;

import ar.db.projeto_votacao.domain.enums.TipoVoto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Associado associado;

    @ManyToOne
    private Pauta pauta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVoto tipoVoto;

    public Voto(Associado associado, Pauta pauta, TipoVoto tipoVoto) {
        this.associado = associado;
        this.pauta = pauta;
        this.tipoVoto = tipoVoto;
    }
}
