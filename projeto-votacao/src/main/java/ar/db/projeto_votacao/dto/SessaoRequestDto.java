package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotNull;

public record SessaoRequestDto(
        @NotNull(message = "Informe o ID da pauta")
        Long pautaId,
        Integer duracao
) {
}
