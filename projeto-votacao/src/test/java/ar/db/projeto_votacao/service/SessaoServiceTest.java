package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.exception.SessionRegisteredException;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private SessaoService sessaoService;

    @Test
    void deveIniciarSessaoComSucessoEDuracaoDefault(){
        Long pautaId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(pautaId);

        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, null);
        Sessao sessao = new Sessao(pauta);

        when(pautaRepository.findById(requestDto.pautaId())).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        var result = sessaoService.iniciarSessao(requestDto);

        assertNotNull(result);
        assertEquals(pauta.getId(), result.pauta().id());
        assertEquals(1, result.duracao());
    }

    @Test
    void deveIniciarSessaoComSucessoEDuracaoEnviada(){
        Long pautaId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(pautaId);

        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, 5);
        Sessao sessao = new Sessao(pauta);

        when(pautaRepository.findById(requestDto.pautaId())).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        var result = sessaoService.iniciarSessao(requestDto);

        assertNotNull(result);
        assertEquals(pauta.getId(), result.pauta().id());
        assertEquals(5, result.duracao());
    }

    @Test
    void deveLancarExceptQuandoExistirUmaSessaoParaPauta(){
        Long pautaId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(pautaId);

        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, 5);
        Sessao sessao = new Sessao(pauta);

        pauta.setSessao(sessao);
        sessao.setPauta(pauta);

        when(sessaoRepository.existsByPautaId(requestDto.pautaId())).thenReturn(Boolean.TRUE);
        when(pautaRepository.findById(requestDto.pautaId())).thenReturn(Optional.of(pauta));

        var result = assertThrows(SessionRegisteredException.class, () ->
                sessaoService.iniciarSessao(requestDto));

        assertEquals("Já existe uma sessão para esta pauta", result.getMessage());
        verify(sessaoRepository, never()).save(any(Sessao.class));
    }
}
