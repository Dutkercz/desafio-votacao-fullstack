package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Sessao;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SessaoResponseDto(
        Long id,
        PautaResponseDto pauta,

        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
        LocalDateTime inicio,

        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
        LocalDateTime fim,

        Integer duracao
) {
    public SessaoResponseDto(Sessao sessao, Integer duracao) {
        this(sessao.getId(), new PautaResponseDto(sessao.getPauta()), sessao.getInicio(), sessao.getFim(), duracao);
    }
}
