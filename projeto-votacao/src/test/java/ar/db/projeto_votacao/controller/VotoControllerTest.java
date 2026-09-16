package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.client.ApiVerificadoraDeCpf;
import ar.db.projeto_votacao.client.RespostaHttpSimulada;
import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.SessaoRepository;
import ar.db.projeto_votacao.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("h2")
@Transactional
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private VotoRepository votoRepository;

    @MockitoBean
    private ApiVerificadoraDeCpf verificadoraDeCpf;

    @Autowired
    private JacksonTester<VotoRequestDto> votoRequestTester;

    @Test
    void deveRegistrarVotoComSucesso() throws Exception {
        Pauta pauta = new Pauta("Titulo da Pauta");
        pautaRepository.save(pauta);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(LocalDateTime.now().plusMinutes(1));
        pauta.setSessao(sessao);
        sessaoRepository.save(sessao);
        pautaRepository.save(pauta);

        Associado associado = new Associado("12345678900");
        associadoRepository.save(associado);

        VotoRequestDto requestDto = new VotoRequestDto(
                associado.getId(), pauta.getId(), TipoVoto.SIM);

        //preciso controlar o retorno da "API externa"(Fake) pra o teste nao ter chance de falhar
        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(200, "ABLE_TO_VOTE");
        when(verificadoraDeCpf.isCpfValido(associado.getCpf())).thenReturn(httpResposta);

        mockMvc.perform(post("/api/v1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(votoRequestTester.write(requestDto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pauta.id").value(pauta.getId()))
                .andExpect(jsonPath("$.tipoVoto").value(TipoVoto.SIM.toString()))
                .andDo(print());
    }

    @Test
    void deveLancarExceptCpfInapto() throws Exception {
        Pauta pauta = new Pauta("Titulo da Pauta");
        pautaRepository.save(pauta);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(LocalDateTime.now().plusMinutes(1));
        pauta.setSessao(sessao);
        sessaoRepository.save(sessao);
        pautaRepository.save(pauta);

        Associado associado = new Associado("12345678900");
        associadoRepository.save(associado);

        VotoRequestDto requestDto = new VotoRequestDto(
                associado.getId(), pauta.getId(), TipoVoto.SIM);

        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(404, "UNABLE_TO_VOTE");
        when(verificadoraDeCpf.isCpfValido(associado.getCpf())).thenReturn(httpResposta);

        mockMvc.perform(post("/api/v1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(votoRequestTester.write(requestDto).getJson()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deveLancarExceptSessaoNaoIniciada() throws Exception {
        Pauta pauta = new Pauta("Titulo da Pauta");
        pautaRepository.save(pauta);

        Associado associado = new Associado("12345678900");
        associadoRepository.save(associado);

        VotoRequestDto requestDto = new VotoRequestDto(
                associado.getId(), pauta.getId(), TipoVoto.SIM);

        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(200, "ABLE_TO_VOTE");
        when(verificadoraDeCpf.isCpfValido(associado.getCpf())).thenReturn(httpResposta);

        String result = mockMvc.perform(post("/api/v1/votos")
                                                         .contentType(MediaType.APPLICATION_JSON).
                                                        content(
                                                votoRequestTester.write(requestDto).getJson()))
                                        .andExpect(status().isNotFound())
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();
        assertTrue(result.contains("Sessão ainda não foi iniciada"));
    }

    @Test
    void deveLancarExceptSessaoFinalizada() throws Exception {
        Pauta pauta = new Pauta("Titulo da Pauta");
        pautaRepository.save(pauta);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(LocalDateTime.of(1995, 8, 10 , 21 , 30));
        pauta.setSessao(sessao);
        sessaoRepository.save(sessao);
        pautaRepository.save(pauta);

        Associado associado = new Associado("12345678900");
        associadoRepository.save(associado);

        VotoRequestDto requestDto = new VotoRequestDto(
                associado.getId(), pauta.getId(), TipoVoto.SIM);

        RespostaHttpSimulada httpResposta = new RespostaHttpSimulada(200, "ABLE_TO_VOTE");
        when(verificadoraDeCpf.isCpfValido(associado.getCpf())).thenReturn(httpResposta);


        String result = mockMvc.perform(post("/api/v1/votos")
                                                         .contentType(MediaType.APPLICATION_JSON).
                                                        content(
                                                votoRequestTester.write(requestDto).getJson()))
                                        .andExpect(status().isBadRequest())
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();
        assertTrue(result.contains("A sessão já foi finalizada"));
    }


}