package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.AssociadoRequestDto;
import ar.db.projeto_votacao.dto.AssociadoResponseDto;
import ar.db.projeto_votacao.service.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/associados")
@RequiredArgsConstructor
@Tag(name = "Associado", description = "Endpoints para gerenciamento de Associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @Operation(summary = "Realiza o cadastro de Associado",
            description = "Ao informar o CPF realiza o cadastro de um Associado")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AssociadoResponseDto.class))}),
             @ApiResponse(responseCode = "400", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    public ResponseEntity<AssociadoResponseDto> novoAssociado(@RequestBody @Valid AssociadoRequestDto requestDto,
                                                              UriComponentsBuilder builder) {
        AssociadoResponseDto responseDto = associadoService.cadastrarAssociado(requestDto);
        URI uri = builder.path("/api/vi/associados/{id}")
                         .buildAndExpand(responseDto.id())
                         .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }
}
