package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.PautaCardDto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.dto.PautaResponseDto;
import ar.db.projeto_votacao.dto.PautaResultadoDto;
import ar.db.projeto_votacao.service.PautaService;
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
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<PautaResponseDto> novaPauta(@RequestBody @Valid PautaRequestDto requestDto,
                                                      UriComponentsBuilder builder) {
        PautaResponseDto responseDto = pautaService.novaPauta(requestDto);
        URI uri = builder.path("/api/v1/pautas/{id}")
                         .buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<PautaCardDto>> pautasNaoVotadas(Pageable pageable){
        return ResponseEntity.ok(pautaService.pautas(pageable));
    }

    @GetMapping("/resultados")
    public ResponseEntity<Page<PautaResultadoDto>> resultadoPauta(Pageable pageable) {
        return ResponseEntity.ok(pautaService.resultadoUltimasPautas(pageable));
    }
}
