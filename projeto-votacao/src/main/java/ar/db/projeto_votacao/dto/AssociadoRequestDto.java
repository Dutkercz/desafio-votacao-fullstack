package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record AssociadoRequestDto(
        @NotBlank
        String nome,

        @NotBlank
        String cpf
) {
}
