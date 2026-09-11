package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotNull;

public record SessaoRequestDto(
        @NotNull
        Long pautaId,
        Integer duracao
) {
}
