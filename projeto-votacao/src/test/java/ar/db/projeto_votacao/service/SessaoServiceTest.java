package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import ar.db.projeto_votacao.domain.enums.SessaoStatus;
import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
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
    void deveIniciarSessaoComSucessoEDuracaoDefault() {
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
    }

    @Test
    void deveIniciarSessaoComSucessoEDuracaoEnviada() {
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
    }

    @Test
    void deveIniciarSessaoComDuracaoInvalidaUsandoValorDefault() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(pautaId);

        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, 0);
        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusSeconds(60L));

        when(pautaRepository.findById(requestDto.pautaId())).thenReturn(Optional.of(pauta));
        when(sessaoRepository.existsByPautaId(pautaId)).thenReturn(false);
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        var result = sessaoService.iniciarSessao(requestDto);

        assertNotNull(result);
        assertEquals(pauta.getId(), result.pauta().id());
        assertEquals(sessao.getFim().getEpochSecond(),result.fim().getEpochSecond());
    }

    @Test
    void deveLancarExceptQuandoExistirUmaSessaoParaPauta() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        pauta.setId(pautaId);

        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, 5);
        Sessao sessao = new Sessao(pauta);

        pauta.setSessao(sessao);
        sessao.setPauta(pauta);

        when(sessaoRepository.existsByPautaId(requestDto.pautaId())).thenReturn(Boolean.TRUE);
        when(pautaRepository.findById(requestDto.pautaId())).thenReturn(Optional.of(pauta));

        var result = assertThrows(SessionRegisteredException.class, () -> sessaoService.iniciarSessao(requestDto));

        assertEquals("Já existe uma sessão para esta pauta", result.getMessage());
        verify(sessaoRepository, never()).save(any(Sessao.class));
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        Long pautaId = 1L;
        SessaoRequestDto requestDto = new SessaoRequestDto(pautaId, 5);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () -> sessaoService.iniciarSessao(requestDto));

        assertEquals("Pauta não encontrada", result.getMessage());
        verify(sessaoRepository, never()).save(any(Sessao.class));
    }

    @Test
    void deveFinalizarSessaoComSucesso() {
        Long sessaoId = 1L;
        Pauta pauta = new Pauta("Titulo da pauta");
        Sessao sessao = new Sessao(pauta);
        sessao.setId(sessaoId);

        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        var result = sessaoService.finalizarSessao(sessaoId);

        assertEquals(sessaoId, result.id());
        assertEquals(SessaoStatus.FINALIZADA, result.status());
        assertEquals(PautaStatus.FINALIZADA, pauta.getStatus());
    }

    @Test
    void deveLancarExcecaoAoFinalizarSessaoInexistente() {
        Long sessaoId = 1L;
        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () -> sessaoService.finalizarSessao(sessaoId));

        assertEquals("Sessão não encontrada", result.getMessage());
    }
}
