package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record PautaRequestDto(
        @NotBlank(message = "O campo titulo não pode estar em branco")
        String titulo) {
}
