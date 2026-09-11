package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.dto.VotoResponseDto;
import ar.db.projeto_votacao.exception.AgendaNotFound;
import ar.db.projeto_votacao.exception.AssociateAlreadyVoted;
import ar.db.projeto_votacao.exception.AssociateNotExistException;
import ar.db.projeto_votacao.exception.SessionAlreadyClosed;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final AssociadoRepository associadoRepository;

    public VotoResponseDto registrarVoto(VotoRequestDto requestDto) {
        Pauta pauta = pautaRepository.findById(requestDto.pautaId())
                        .orElseThrow(() -> new AgendaNotFound("Pauta não encontrada"));

        Sessao sessao = pauta.getSessao();
        if(sessao.estaEncerrada()){
            throw new SessionAlreadyClosed("A sessão já foi finalizada");
        }

        Associado associado = associadoRepository.findById(requestDto.associadoId()).orElseThrow(
                () -> new AssociateNotExistException("Associado não encontrado"));

        pauta.getVotos().stream()
             .filter(
                     voto -> voto.getAssociado().getId().equals(associado.getId()))
             .findFirst()
             .ifPresent(voto -> {
                 throw new AssociateAlreadyVoted("Associado já participou da votação");
             });
        Voto voto = votoRepository.save(new Voto(associado, pauta, requestDto.tipoVoto()));
        return new VotoResponseDto(voto);
    }
}
