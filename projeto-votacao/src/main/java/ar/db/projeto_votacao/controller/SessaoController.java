package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.dto.SessaoResponseDto;
import ar.db.projeto_votacao.service.SessaoService;
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
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<SessaoResponseDto> abrirSessao(@RequestBody @Valid SessaoRequestDto requestDto,
                                                         UriComponentsBuilder builder) {
        SessaoResponseDto responseDto = sessaoService.iniciarSessao(requestDto);
        URI uri = builder.path("/api/v1/sessoes/{id}")
                    .buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SessaoResponseDto> finalizarSessao(@PathVariable Long id){
        return ResponseEntity.ok(sessaoService.finalizarSessao(id));
    }
}
