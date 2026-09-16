package ar.db.projeto_votacao.controller;

import ar.db.projeto_votacao.dto.AssociadoRequestDto;
import ar.db.projeto_votacao.repository.AssociadoRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("h2")
@Transactional
class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AssociadoRequestDto> associadoRequestTester;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    void deveCadastrarAssociadoComSucesso() throws Exception {
        AssociadoRequestDto requestDto = new AssociadoRequestDto("12345678900");

        mockMvc.perform(post("/api/v1/associados")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(associadoRequestTester.write(requestDto).getJson()))
                .andExpect(status().isCreated())
                .andDo(print());
        assertEquals(1, associadoRepository.count(), "Deve ter 1 associado registrado");
    }

    @Test
    void deveLancarExceptionCpfForaDeFormatoEsperado() throws Exception {
        //cpf com menos de 11 digitos
        AssociadoRequestDto requestDto = new AssociadoRequestDto("123456789");

        mockMvc.perform(post("/api/v1/associados")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(associadoRequestTester.write(requestDto).getJson()))
               .andExpect(status().isBadRequest())
               .andDo(print());
        assertEquals(0, associadoRepository.count(), "Deve ter 1 associado registrado");
    }

}