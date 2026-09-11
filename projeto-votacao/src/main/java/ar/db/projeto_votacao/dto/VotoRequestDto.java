package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.enums.TipoVoto;
import jakarta.validation.constraints.NotNull;

public record VotoRequestDto(
        @NotNull
        Long associadoId,
        @NotNull
        Long pautaId,
        @NotNull
        TipoVoto tipoVoto) {
}
