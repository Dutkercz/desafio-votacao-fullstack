package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.dto.AssociadoRequestDto;
import ar.db.projeto_votacao.exception.AssociateRegisteredException;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    @Test
    void deveSalvarNovoAssociadoComSucesso(){
        String cpf = "12345678911";
        AssociadoRequestDto requestDto = new AssociadoRequestDto(cpf);
        Associado associado = new Associado(cpf);


        when(associadoRepository.existsByCpf(cpf)).thenReturn(Boolean.FALSE);
        when(associadoRepository.save(associado)).thenReturn(associado);

        var result = associadoService.cadastrarAssociado(requestDto);
        assertNotNull(result);
    }

    @Test
    void deveLancarExceptionAssociadoJaCadastrado(){
        String cpf = "12345678911";
        AssociadoRequestDto requestDto = new AssociadoRequestDto(cpf);

        when(associadoRepository.existsByCpf(cpf)).thenReturn(Boolean.TRUE);

        var result = assertThrows(AssociateRegisteredException.class, () ->
                        associadoService.cadastrarAssociado(requestDto));

        assertEquals("Associado já cadastrado", result.getMessage());
        verify(associadoRepository, never()).save(any(Associado.class));
    }

}
