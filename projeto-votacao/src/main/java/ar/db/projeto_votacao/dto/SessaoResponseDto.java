package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Sessao;
import ar.db.projeto_votacao.domain.enums.SessaoStatus;

import java.time.LocalDateTime;

public record SessaoResponseDto(
        Long id,
        PautaResponseDto pauta,
        LocalDateTime inicio,
        LocalDateTime fim,
        SessaoStatus status)
{
    public SessaoResponseDto(Sessao sessao) {
        this(sessao.getId(), new PautaResponseDto(sessao.getPauta()), sessao.getInicio(), sessao.getFim(), sessao.getStatus());
    }
}
