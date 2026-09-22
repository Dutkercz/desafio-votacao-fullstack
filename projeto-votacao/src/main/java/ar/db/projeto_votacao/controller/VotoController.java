package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.SessaoResponseDto;
import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.dto.VotoResponseDto;
import ar.db.projeto_votacao.service.VotoService;
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
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
@Tag(name = "Voto", description = "Endpoints para gerenciamento de Votos")
public class VotoController {

    private final VotoService votoService;

    @Operation(summary = "Registra o Voto",
            description = "Registra o voto em uma sessão, que deve estar ativa")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = VotoResponseDto.class))}),
             @ApiResponse(responseCode = "400", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    public ResponseEntity<VotoResponseDto> registrarVoto(@RequestBody @Valid VotoRequestDto requestDto,
                                                         UriComponentsBuilder builder) {
        VotoResponseDto votoResponseDto = votoService.registrarVoto(requestDto);
        URI uri = builder.path("/api/v1/votos/{id}")
                         .buildAndExpand(votoResponseDto.pauta()).toUri();
        return ResponseEntity.created(uri).body(votoResponseDto);
    }
}
