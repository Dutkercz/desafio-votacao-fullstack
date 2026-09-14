package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.PautaRequestDto;
import ar.db.projeto_votacao.dto.PautaResponseDto;
import ar.db.projeto_votacao.dto.PautaResultadoDto;
import ar.db.projeto_votacao.exception.AgendaNotFound;
import ar.db.projeto_votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PautaResultadoDto resultadoDaPauta(Long id){
        Pauta pauta = pautaRepository.findById(id)
                        .orElseThrow(() -> new AgendaNotFound("Pauta não encontrada"));

        long totalVotosSim = pauta.getVotos()
                                    .stream().filter(voto -> voto.getTipoVoto() == TipoVoto.SIM)
                                    .count();
        long totalVotosNao = pauta.getVotos()
                                    .stream().filter(voto -> voto.getTipoVoto() == TipoVoto.NAO)
                                    .count();
        long totalVotos = totalVotosSim + totalVotosNao;

        return new PautaResultadoDto(pauta.getId(), pauta.getTitulo(), totalVotos, totalVotosSim, totalVotosNao);
    }
}
