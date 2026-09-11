package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.dto.AssociadoRequestDto;
import ar.db.projeto_votacao.dto.AssociadoResponseDto;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final AssociadoRepository associadoRepository;

    public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto requestDto) {
        Associado associado = new Associado(requestDto.nome(), requestDto.cpf());
        associadoRepository.save(associado);
        return new AssociadoResponseDto(associado);
    }
}
