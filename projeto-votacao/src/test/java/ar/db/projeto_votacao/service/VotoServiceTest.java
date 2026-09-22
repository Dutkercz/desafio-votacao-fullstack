package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.client.ApiVerificadoraDeCpf;
import ar.db.projeto_votacao.client.RespostaHttpSimulada;
import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.SessaoStatus;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.exception.AssociateAlreadyVotedException;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.exception.SessionClosedException;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.VotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private AssociadoRepository associadoRepository;
    @Mock
    private ApiVerificadoraDeCpf apiVerificadoraDeCpf;

    @InjectMocks
    private VotoService votoService;

    @Test
    void deveRegistrarVotoComSucesso() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String cpf = "12345678911";
        String titulo = "Titulo da Pauta";

        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(200, "ABLE_TO_VOTE");

        Associado associado = new Associado(cpf);
        associado.setId(associadoId);

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusMinutes(1));
        pauta.setSessao(sessao);

        Voto voto = new Voto(associado, pauta, tipoVoto);
        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(associadoRepository.findById(associadoId)).thenReturn(Optional.of(associado));
        when(votoRepository.save(voto)).thenReturn(voto);
        when(apiVerificadoraDeCpf.isCpfValido(cpf)).thenReturn(httpResposta);

        var result = votoService.registrarVoto(requestDto);

        assertNotNull(result);
        assertEquals(tipoVoto, result.tipoVoto());
        assertEquals(pautaId, result.pauta().id());
    }

    @Test
    void deveLancarExceptionPautaNaoEncontrada() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;

        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () ->
                votoService.registrarVoto(requestDto));
        assertEquals("Pauta não encontrada", result.getMessage());
    }

    @Test
    void deveLancarExceptionSessaoNaoIniciada() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String titulo = "Titulo da Pauta";

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        var result = assertThrows(ResourceNotFoundException.class, () ->
                votoService.registrarVoto(requestDto));

        assertEquals("Sessão ainda não foi iniciada", result.getMessage());
    }

    @Test
    void deveLancarExceptionSessaoFinalizada() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String titulo = "Titulo da Pauta";

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(LocalDateTime.of(1992, 8, 10, 21, 30));
        sessao.setStatus(SessaoStatus.FINALIZADA);
        pauta.setSessao(sessao);

        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        var result = assertThrows(SessionClosedException.class, () ->
                votoService.registrarVoto(requestDto));

        assertEquals("A sessão já foi finalizada", result.getMessage());
    }

    @Test
    void deveLancarExceptionAssociadoNaoEncontrado() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String cpf = "12345678911";
        String titulo = "Titulo da Pauta";

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusMinutes(1));
        pauta.setSessao(sessao);

        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(associadoRepository.findById(associadoId)).thenReturn(Optional.empty());

        var result = assertThrows(ResourceNotFoundException.class, () ->
                votoService.registrarVoto(requestDto));

        assertEquals("Associado não encontrado", result.getMessage());
    }

    @Test
    void deveLancarExceptionAssociadoJaVotou() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String cpf = "12345678911";
        String titulo = "Titulo da Pauta";

        Associado associado = new Associado(cpf);
        associado.setId(associadoId);

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusMinutes(1));
        pauta.setSessao(sessao);

        Voto voto = new Voto(associado, pauta, tipoVoto);
        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);
        pauta.setVotos(new ArrayList<>(List.of(voto)));

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(associadoRepository.findById(associadoId)).thenReturn(Optional.of(associado));

        var result = assertThrows(AssociateAlreadyVotedException.class, () ->
                votoService.registrarVoto(requestDto));

        assertEquals("Associado já participou da votação", result.getMessage());
    }

    @Test
    void deveLancarExceptionCpfNaoValidoParaVotacao() {
        Long associadoId = 1L;
        Long pautaId = 11L;
        TipoVoto tipoVoto = TipoVoto.SIM;
        String cpf = "12345678911";
        String titulo = "Titulo da Pauta";

        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(404, "UNABLE_TO_VOTE");

        Associado associado = new Associado(cpf);
        associado.setId(associadoId);

        Pauta pauta = new Pauta(titulo);
        pauta.setId(pautaId);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusMinutes(1));
        pauta.setSessao(sessao);

        VotoRequestDto requestDto = new VotoRequestDto(associadoId, pautaId, tipoVoto);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(associadoRepository.findById(associadoId)).thenReturn(Optional.of(associado));
        when(apiVerificadoraDeCpf.isCpfValido(cpf)).thenReturn(httpResposta);

        var result = assertThrows(ResourceNotFoundException.class, () ->
                votoService.registrarVoto(requestDto));

        assertEquals("UNABLE_TO_VOTE", result.getMessage());
    }

}
