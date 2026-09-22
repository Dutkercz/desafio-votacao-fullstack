package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.PautaCardDto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.dto.PautaResultadoDto;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
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
    void deveRegistrarPautaComSucesso() {
        String titulo = "Titulo da pauta";
        Pauta pauta = new Pauta(titulo);
        PautaRequestDto requestDto = new PautaRequestDto(titulo);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        var result = pautaService.novaPauta(requestDto);

        assertNotNull(result);
        assertEquals(titulo, result.titulo());
    }

    @Test
    void deveRetornarPautaAoBuscarPorId() {
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        var result = pautaService.getPauta(1L);

        assertNotNull(result);
        assertEquals(pauta.getTitulo(), result.titulo());
        assertEquals(pauta.getId(), result.id());
    }

    @Test
    void deveLancarExcecaoNaoEncontrarPauta() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () -> pautaService.getPauta(1L));

        assertEquals("Pauta não encontrada", result.getMessage());
        verify(pautaRepository, never()).save(any(Pauta.class));
    }

    @Test
    void deveRetornarResultadoDaPauta() {
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

    @Test
    void deveRetornarPautasNaoFinalizadasComSessao() {
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);
        Sessao sessao = new Sessao(pauta);
        pauta.setSessao(sessao);
        PageRequest pageable = PageRequest.of(0, 10);

        when(pautaRepository.findNaoFinalizadosOrdenados(pageable))
                .thenReturn(new PageImpl<>(List.of(pauta), pageable, 1));

        Page<PautaCardDto> result = pautaService.listarPautasPaginada(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(pauta.getId(), result.getContent().getFirst().id());
        assertNotNull(result.getContent().getFirst().sessao());
    }

    @Test
    void deveRetornarPautaNaoFinalizadaSemSessao() {
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);
        PageRequest pageable = PageRequest.of(0, 10);

        when(pautaRepository.findNaoFinalizadosOrdenados(pageable))
                .thenReturn(new PageImpl<>(List.of(pauta), pageable, 1));

        Page<PautaCardDto> result = pautaService.listarPautasPaginada(pageable);

        assertNull(result.getContent().getFirst().sessao());
        assertEquals(pauta.getTitulo(), result.getContent().getFirst().titulo());
    }

    @Test
    void deveRetornarResultadosDasUltimasPautas() {
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);
        pauta.setVotos(new ArrayList<>());
        PageRequest pageable = PageRequest.of(0, 10);

        when(pautaRepository.findPorStatus(PautaStatus.FINALIZADA, pageable))
                .thenReturn(new PageImpl<>(List.of(pauta), pageable, 1));
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Page<PautaResultadoDto> result = pautaService.resultadoUltimasPautas(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(pauta.getTitulo(), result.getContent().getFirst().titulo());
        assertEquals(0L, result.getContent().getFirst().totalVotos());
    }

    @Test
    void deveRetornarTotaisZeradosQuandoPautaNaoPossuiVotos() {
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(1L);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        PautaResultadoDto result = pautaService.resultadoDaPauta(pauta.getId());

        assertEquals(0L, result.totalVotos());
        assertEquals(0L, result.totalVotosSim());
        assertEquals(0L, result.totalVotosNao());
    }

}
