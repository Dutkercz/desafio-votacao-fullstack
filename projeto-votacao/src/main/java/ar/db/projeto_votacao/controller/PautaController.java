package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.PautaCardDto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.dto.PautaResponseDto;
import ar.db.projeto_votacao.dto.PautaResultadoDto;
import ar.db.projeto_votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pauta", description = "Endpoints para gerenciamento de Pautas")
public class PautaController {

    private final PautaService pautaService;

    @Operation(summary = "Realiza o cadastro de Pautas",
            description = "Ao informar o Titulo realiza o cadastro de uma nova Pauta")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PautaResponseDto.class))}),
             @ApiResponse(responseCode = "400", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    public ResponseEntity<PautaResponseDto> novaPauta(@RequestBody @Valid PautaRequestDto requestDto,
                                                      UriComponentsBuilder builder) {
        PautaResponseDto responseDto = pautaService.novaPauta(requestDto);
        URI uri = builder.path("/api/v1/pautas/{id}")
                         .buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Operation(summary = "Listar Pautas Ativas",
            description = "Lista todas as Pautas ativas, como ou sem seessão iniciada, de forma páginada")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PautaCardDto.class)))}),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping
    public ResponseEntity<Page<PautaCardDto>> pautasNaoVotadas(Pageable pageable){
        return ResponseEntity.ok(pautaService.listarPautasPaginada(pageable));
    }

    @Operation(summary = "Listar Pautas com Resultado",
            description = "Lista Pautas finalizadas, com o total de votos, total de votos SIM e NÂO, de forma páginada")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PautaResultadoDto.class)))}),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("/resultados")
    public ResponseEntity<Page<PautaResultadoDto>> resultadoPauta(Pageable pageable) {
        return ResponseEntity.ok(pautaService.resultadoUltimasPautas(pageable));
    }
}
