package ar.db.projeto_votacao.service;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.dto.SessaoRequestDto;
import ar.db.projeto_votacao.dto.SessaoResponseDto;
import ar.db.projeto_votacao.exception.AgendaNotFound;
import ar.db.projeto_votacao.exception.SessionAlreadyRegistered;
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
                        .orElseThrow(() -> new AgendaNotFound("Pauta não encontrada"));

        if (pauta.getSessao() != null){
            throw new SessionAlreadyRegistered("Já existe uma sessão para esta pauta");
        }

        Sessao sessao = new Sessao(pauta);
        int duracao = requestDto.duracao() != null && requestDto.duracao() > 0 ? requestDto.duracao() : 1;
        sessao.setFim(sessao.getInicio().plusMinutes(duracao));

        sessaoRepository.save(sessao);
        pauta.setSessao(sessao);
        return new SessaoResponseDto(sessao);
    }
}
