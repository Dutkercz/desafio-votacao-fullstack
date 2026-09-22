package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.enums.PautaStatus;
import ar.db.projeto_votacao.domain.enums.SessaoStatus;
import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.dto.SessaoResponseDto;
import ar.db.projeto_votacao.exception.ResourceNotFoundException;
import ar.db.projeto_votacao.exception.SessionRegisteredException;
import ar.db.projeto_votacao.repository.PautaRepository;
import ar.db.projeto_votacao.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;

    @Transactional
    public SessaoResponseDto iniciarSessao(SessaoRequestDto requestDto) {
        Pauta pauta = pautaRepository.findById(requestDto.pautaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));

        //uma sessão de votação por pauta
        if (sessaoRepository.existsByPautaId(pauta.getId())){
            throw new SessionRegisteredException("Já existe uma sessão para esta pauta");
        }
        Sessao sessao = new Sessao(pauta);

        // por default 1 minuto por sessão
        int duracao = requestDto.duracao() != null && requestDto.duracao() > 0 ? requestDto.duracao() : 1;
        sessao.setFim(sessao.getInicio().plusMinutes(duracao));
        sessao.setStatus(SessaoStatus.EM_ANDAMENTO);
        pauta.setStatus(PautaStatus.EM_VOTACAO);

        sessaoRepository.save(sessao);
        pauta.setSessao(sessao);
        return new SessaoResponseDto(sessao);
    }

    @Transactional
    public SessaoResponseDto finalizarSessao(Long id) {
        Sessao sessao = sessaoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Sessão não encontrada"));
        sessao.setStatus(SessaoStatus.FINALIZADA);
        sessao.getPauta().setStatus(PautaStatus.FINALIZADA);
        return new SessaoResponseDto(sessao);
    }
}
