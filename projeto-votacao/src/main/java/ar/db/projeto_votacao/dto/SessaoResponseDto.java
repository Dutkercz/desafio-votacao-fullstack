package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Sessao;

import java.time.LocalDateTime;

public record SessaoResponseDto(
        Long id,
        PautaResponseDto pauta,
        LocalDateTime inicio,
        LocalDateTime fim
) {
    public SessaoResponseDto(Sessao sessao) {
        this(sessao.getId(), new PautaResponseDto(sessao.getPauta()), sessao.getInicio(), sessao.getFim());
    }
}
