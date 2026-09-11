package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Associado;

public record AssociadoResponseDto(
        Long id,
        String nome
) {
    public AssociadoResponseDto(Associado associado) {
        this(associado.getId(), associado.getNome());
    }
}
