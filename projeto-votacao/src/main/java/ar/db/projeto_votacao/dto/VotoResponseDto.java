package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.TipoVoto;

public record VotoResponseDto(
        Long id,
        PautaResponseDto pauta,
        TipoVoto tipoVoto
) {
    public VotoResponseDto(Voto voto) {
        this(voto.getId(), new PautaResponseDto(voto.getPauta()), voto.getTipoVoto());
    }
}
