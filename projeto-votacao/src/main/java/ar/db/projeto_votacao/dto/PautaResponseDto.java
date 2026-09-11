package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Pauta;

public record PautaResponseDto(
        Long id,
        String titulo
) {
    public PautaResponseDto(Pauta pauta) {
        this(pauta.getId(), pauta.getTitulo());
    }
}
