package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.client.ApiVerificadoraDeCpf;
import ar.db.projeto_votacao.domain.Associado;
import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.Voto;
import ar.db.projeto_votacao.dto.VotoRequestDto;
import ar.db.projeto_votacao.dto.VotoResponseDto;
import ar.db.projeto_votacao.exception.AssociateAlreadyVotedException;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.exception.SessionClosedException;
import ar.db.projeto_votacao.repository.AssociadoRepository;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final AssociadoRepository associadoRepository;
    private final ApiVerificadoraDeCpf apiVerificadoraDeCpf;

    @Transactional
    public VotoResponseDto registrarVoto(VotoRequestDto requestDto) {
        Pauta pauta = pautaRepository.findById(requestDto.pautaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));

        Sessao sessao = pauta.getSessao();
        if (sessao == null) {
            throw new ResourceNotFoundException("Sessão ainda não foi iniciada");
        }
        if(sessao.getStatus().isEncerrada()){
            throw new SessionClosedException("A sessão já foi finalizada");
        }

        Associado associado = associadoRepository.findById(requestDto.associadoId()).orElseThrow(
                () -> new ResourceNotFoundException("Associado não encontrado"));

        pauta.getVotos().stream()
             .filter(
                     voto -> voto.getAssociado().getId().equals(associado.getId()))
             .findFirst()
             .ifPresent(voto -> {
                 throw new AssociateAlreadyVotedException("Associado já participou da votação");
             });

        var resposta = apiVerificadoraDeCpf.isCpfValido(associado.getCpf());
        log.info("Resposta API externa {}", resposta);
        if (resposta.statusCode() != 200) {
            throw new ResourceNotFoundException(resposta.status());
        }

        Voto voto = votoRepository.save(new Voto(associado, pauta, requestDto.tipoVoto()));
        return new VotoResponseDto(voto);
    }
}
