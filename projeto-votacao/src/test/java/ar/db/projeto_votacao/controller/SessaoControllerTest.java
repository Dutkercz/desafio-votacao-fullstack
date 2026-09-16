package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("h2")
@Transactional
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<SessaoRequestDto> sessaoDtoTester;

    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void deveIniciarSessaoComDuracaoDefault() throws Exception{
        Pauta pauta = pautaRepository.save(new Pauta("Titulo da Pauta"));
        SessaoRequestDto requestDto = new SessaoRequestDto(pauta.getId(), null);

        mockMvc.perform(post("/api/v1/sessoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sessaoDtoTester.write(requestDto).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pauta.id").value(pauta.getId()))
                .andExpect(jsonPath("$.pauta.titulo").value(pauta.getTitulo()))
                .andExpect(jsonPath("$.duracao").value(1))
                .andDo(print());
    }

    @Test
    void deveIniciarSessaoComDuracaoPersonalizada() throws Exception{
        Pauta pauta = pautaRepository.save(new Pauta("Titulo da Pauta"));
        SessaoRequestDto requestDto = new SessaoRequestDto(pauta.getId(), 5);

        mockMvc.perform(post("/api/v1/sessoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sessaoDtoTester.write(requestDto).getJson()))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.pauta.id").value(pauta.getId()))
               .andExpect(jsonPath("$.pauta.titulo").value(pauta.getTitulo()))
               .andExpect(jsonPath("$.duracao").value(5))
               .andDo(print());
    }


    @Test
    void deveLancarExceptQuandoPautaIdNullo() throws Exception{
        SessaoRequestDto requestDto = new SessaoRequestDto(1L, 0);

        mockMvc.perform(post("/api/v1/sessoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sessaoDtoTester.write(requestDto).getJson()))
               .andExpect(status().isNotFound())
               .andDo(print());
        assertEquals(0, sessaoRepository.count());
    }

    @Test
    void deveLancarExceptPautaJaFoiVotada() throws Exception{
        Pauta pauta = new Pauta("Titulo da Pauta");
        pautaRepository.save(pauta);

        Sessao sessao = new Sessao(pauta);
        sessao.setFim(LocalDateTime.now().plusMinutes(1));
        pauta.setSessao(sessao);

        sessaoRepository.save(sessao);

        SessaoRequestDto requestDto = new SessaoRequestDto(pauta.getId(), 1);

        mockMvc.perform(post("/api/v1/sessoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(sessaoDtoTester.write(requestDto).getJson()))
               .andExpect(status().isConflict())
               .andDo(print());
    }
}