package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.enums.PautaStatus;

public record PautaResponseDto(
        Long id,
        String titulo,
        PautaStatus status
) {
    public PautaResponseDto(Pauta pauta) {
        this(pauta.getId(), pauta.getTitulo(), pauta.getStatus());
    }
}
