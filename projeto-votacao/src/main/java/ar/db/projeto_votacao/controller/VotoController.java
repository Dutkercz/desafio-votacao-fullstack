package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.dto.VotoResponseDto;
import ar.db.projeto_votacao.service.VotoService;
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
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    public ResponseEntity<VotoResponseDto> registrarVoto(@RequestBody VotoRequestDto requestDto,
                                                         UriComponentsBuilder builder) {
        VotoResponseDto votoResponseDto = votoService.registrarVoto(requestDto);
        URI uri = builder.path("/api/v1/votos/{id}")
                         .buildAndExpand(votoResponseDto.pauta()).toUri();
        return ResponseEntity.created(uri).body(votoResponseDto);
    }
}
