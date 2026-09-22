package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.enums.SessaoStatus;

import java.time.Instant;

public record SessaoResponseDto(
        Long id,
        PautaResponseDto pauta,
        Instant inicio,
        Instant fim,
        SessaoStatus status)
{
    public SessaoResponseDto(Sessao sessao) {
        this(sessao.getId(), new PautaResponseDto(sessao.getPauta()),
             sessao.getInicio(), sessao.getFim(), sessao.getStatus());
    }
}
