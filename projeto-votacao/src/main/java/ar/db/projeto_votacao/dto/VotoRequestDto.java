package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.enums.TipoVoto;
import jakarta.validation.constraints.NotNull;

public record VotoRequestDto(
        @NotNull(message = "Informe o ID do associado")
        Long associadoId,
        @NotNull(message = "Informe o ID da pauta")
        Long pautaId,
        @NotNull(message = "O campo tipoVoto não pode estar em branco")
        TipoVoto tipoVoto) {
}
