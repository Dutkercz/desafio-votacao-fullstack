package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.dto.SessaoResponseDto;
import ar.db.projeto_votacao.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessão", description = "Endpoints para gerenciamento de Sessões")
public class SessaoController {

    private final SessaoService sessaoService;


    @Operation(summary = "Realiza o inicio de Sessão",
            description = "Inicia uma sessão votação")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SessaoResponseDto.class))}),
             @ApiResponse(responseCode = "400", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    public ResponseEntity<SessaoResponseDto> abrirSessao(@RequestBody @Valid SessaoRequestDto requestDto,
                                                         UriComponentsBuilder builder) {
        SessaoResponseDto responseDto = sessaoService.iniciarSessao(requestDto);
        URI uri = builder.path("/api/v1/sessoes/{id}")
                    .buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Operation(summary = "Finaliza a Sessão",
            description = "Realiza o final da sessão, mudando o status e não aceitando mais votos")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SessaoResponseDto.class))}),
             @ApiResponse(responseCode = "400", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<SessaoResponseDto> finalizarSessao(@PathVariable Long id){
        return ResponseEntity.ok(sessaoService.finalizarSessao(id));
    }
}
