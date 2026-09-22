package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import ar.db.projeto_votacao.domain.enums.TipoVoto;
import ar.db.projeto_votacao.dto.*;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));
        return new PautaResponseDto(pauta);
    }

    public PautaResultadoDto resultadoDaPauta(Long id){
        Pauta pauta = pautaRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));
        long totalVotosSim = contarVotos( pauta.getVotos(),TipoVoto.SIM);
        long totalVotosNao = contarVotos(pauta.getVotos(), TipoVoto.NAO);
        long totalVotos = totalVotosSim + totalVotosNao;
        return new PautaResultadoDto(pauta.getId(), pauta.getTitulo(), totalVotos, totalVotosSim, totalVotosNao);
    }

    public Page<PautaResultadoDto> resultadoUltimasPautas(Pageable pageable){
        return pautaRepository.findPorStatus(PautaStatus.FINALIZADA, pageable)
                              .map(pauta -> resultadoDaPauta(pauta.getId()));
    }

    public Page<PautaCardDto> listarPautasPaginada(Pageable pageable) {
        return  pautaRepository.findNaoFinalizadosOrdenados(pageable).map(this::convertParaPautaCardDto);
    }

    ///Private > Méthodos auxiliares do service
    private PautaCardDto convertParaPautaCardDto(Pauta p) {
        var sessao = p.getSessao();
        if(sessao == null){
            return new PautaCardDto(p.getId(), p.getTitulo(), null, p.getStatus());
        }
        return new PautaCardDto(p, new SessaoResponseDto(sessao));
    }

    private Long contarVotos(List<Voto> votos, TipoVoto tipoVoto){
        if(votos == null) return null;
        return votos.stream()
                    .filter(voto -> voto.getTipoVoto() == tipoVoto)
                    .count();
    }
}
