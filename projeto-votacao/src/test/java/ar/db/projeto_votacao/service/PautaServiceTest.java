package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void deveRegistrarPautaComSucesso(){
        String titulo = "Titulo da pauta";
        Pauta pauta = new Pauta(titulo);
        PautaRequestDto requestDto = new PautaRequestDto(titulo);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        var result = pautaService.novaPauta(requestDto);

        assertNotNull(result);
        assertEquals(titulo, result.titulo());
    }

    @Test
    void deveRetornarPautaAoBuscarPorId(){
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        var result = pautaService.getPauta(1L);

        assertNotNull(result);
        assertEquals(pauta.getTitulo(), result.titulo());
        assertEquals(pauta.getId(),result.id());
    }

    @Test
    void deveLancarExcecaoNaoEncontrarPauta(){
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () ->
                                 pautaService.getPauta(1L));

        assertEquals("Pauta não encontrada", result.getMessage());
        verify(pautaRepository, never()).save(any(Pauta.class));
    }

    @Test
    void deveRetornarResultadoDaPauta(){
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);

        Voto votoUm = new Voto(null, pauta, TipoVoto.SIM);
        Voto votoDois = new Voto(null, pauta, TipoVoto.SIM);
        Voto votoTres = new Voto(null, pauta, TipoVoto.NAO);

        pauta.setVotos(new ArrayList<>(List.of(votoUm, votoDois, votoTres)));

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        var result = pautaService.resultadoDaPauta(1L);

        assertNotNull(result);
        assertEquals(pauta.getId(), result.id());
        assertEquals(pauta.getTitulo(), result.titulo());
        assertEquals(3L, result.totalVotos());
        assertEquals(2L, result.totalVotosSim());
        assertEquals(1L, result.totalVotosNao());
    }

}
