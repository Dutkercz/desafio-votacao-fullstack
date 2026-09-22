package ar.db.projeto_votacao.dto;

import ar.db.projeto_votacao.domain.Pauta;
import ar.db.projeto_votacao.domain.enums.PautaStatus;

public record PautaCardDto(Long id,
                           String titulo,
                           SessaoResponseDto sessao,
                           PautaStatus status
) {
    public PautaCardDto(Pauta p, SessaoResponseDto sessao) {
        this(p.getId(), p.getTitulo(), sessao, p.getStatus());
    }
}