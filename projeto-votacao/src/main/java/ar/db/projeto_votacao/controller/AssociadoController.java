package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.AssociadoRequestDto;
import ar.db.projeto_votacao.dto.AssociadoResponseDto;
import ar.db.projeto_votacao.service.AssociadoService;
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
public class AssociadoController {

    private final AssociadoService associadoService;

    @PostMapping
    public ResponseEntity<AssociadoResponseDto> novoAssociado(@RequestBody @Valid AssociadoRequestDto requestDto,
                                                              UriComponentsBuilder builder) {
        AssociadoResponseDto responseDto = associadoService.cadastrarAssociado(requestDto);
        URI uri = builder.path("/api/vi/associado/{id}")
                         .buildAndExpand(responseDto.id())
                         .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }
}
