package ar.db.projeto_votacao.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AssociadoRequestDto(
        @NotBlank(message = "O campo CPF não pode estar em branco")
        @Pattern(regexp = "\\d{11}", message = "O campo cpf deve conter 11 dígitos")
        //Não irei utilizar a validação de CPF (@CPF) pra este projeto por facilidade no cadastro,
        //apenas o length de 11 digitos vai ser verificado
        @Parameter(required = true)
        String cpf
) {
}
