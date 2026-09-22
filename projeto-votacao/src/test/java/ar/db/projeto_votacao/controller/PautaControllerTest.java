package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("h2")
@Transactional
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<PautaRequestDto> pautaRequestTester;

    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private VotoRepository votoRepository;

    @Test
    void deveCadastrarNovoPauta() throws Exception {
        PautaRequestDto requestDto = new PautaRequestDto("Titulo da Pauta");

        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pautaRequestTester.write(requestDto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Titulo da Pauta"))
                .andDo(print());
        assertEquals(1, pautaRepository.count());
    }

    @Test
    void deveLancarExceptionTituloNullo() throws Exception {
        PautaRequestDto requestDto = new PautaRequestDto("");

        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pautaRequestTester.write(requestDto).getJson()))
                .andExpect(status().isBadRequest())
                .andDo(print());
        assertEquals(0, pautaRepository.count());
    }

    @Test
    void deveRetornarDtoResultadoDaPauta() throws Exception {
        Pauta pauta = pautaRepository.save(new Pauta("Titulo da Pauta"));
        Sessao sessao = new Sessao(pauta);
        sessao.setFim(sessao.getInicio().plusSeconds(60L));
        pauta.setSessao(sessao);

        sessaoRepository.save(sessao);

        Associado associado = new Associado("12345678900");
        associadoRepository.save(associado);

        Voto votoUm = new Voto(associado, pauta, TipoVoto.SIM);
        Voto votoDois = new Voto(associado, pauta, TipoVoto.SIM);
        Voto votoTres = new Voto(associado, pauta, TipoVoto.NAO);

        List<Voto> votos = List.of(votoUm, votoDois, votoTres);
        votoRepository.saveAll(votos);
        pauta.setVotos(votos);
        pauta.setStatus(PautaStatus.FINALIZADA);

        mockMvc.perform(get("/api/v1/pautas/resultados", pauta.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].titulo").value("Titulo da Pauta"))
                .andExpect(jsonPath("$.content[0].totalVotos").value(3))
                .andExpect(jsonPath("$.content[0].totalVotosSim").value(2))
                .andExpect(jsonPath("$.content[0].totalVotosNao").value(1))
                .andDo(print());
    }

    @Test
    void deveListarPautasNaoFinalizadas() {
        Pauta pauta = pautaRepository.save(new Pauta("Pauta ativa"));

        assertEquals(1, pautaRepository.count());
        org.assertj.core.api.Assertions.assertThatCode(() -> mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(pauta.getId()))
                .andExpect(jsonPath("$.content[0].titulo").value("Pauta ativa")))
                .doesNotThrowAnyException();
    }

    @Test
    void deveListarResultadosDasPautasFinalizadas() throws Exception {
        Pauta pauta = new Pauta("Pauta finalizada");
        pauta.setStatus(PautaStatus.FINALIZADA);
        pauta.setVotos(new java.util.ArrayList<>());
        pautaRepository.save(pauta);

        mockMvc.perform(get("/api/v1/pautas/resultados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].titulo").value("Pauta finalizada"))
                .andExpect(jsonPath("$.content[0].totalVotos").value(0))
                .andDo(print());
    }
}