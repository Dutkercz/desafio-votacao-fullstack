package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record PautaRequestDto(
        @NotBlank
        String titulo) {
}
