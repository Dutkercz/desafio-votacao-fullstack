package ar.db.projeto_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AssociadoRequestDto(
        @NotBlank(message = "O campo CPF não pode estar em branco")
        @Pattern(regexp = "\\d{11}", message = "O campo cpf deve conter 11 dígitos")
        String cpf
) {
}
