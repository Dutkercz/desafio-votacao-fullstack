package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.dto.PautaResponseDto;
import ar.db.projeto_votacao.exception.AgendaNotFound;
import ar.db.projeto_votacao.repository.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    @Transactional
    public PautaResponseDto novaPauta(PautaRequestDto pautaRequestDto) {
        Pauta pauta = pautaRepository.save(new Pauta(pautaRequestDto.titulo()));
        return new PautaResponseDto(pauta);
    }

    public PautaResponseDto getPauta(Long id){
        Pauta pauta = pautaRepository.findById(id)
                        .orElseThrow(() -> new AgendaNotFound("Pauta não encontrada"));
        return new PautaResponseDto(pauta);
    }
}
