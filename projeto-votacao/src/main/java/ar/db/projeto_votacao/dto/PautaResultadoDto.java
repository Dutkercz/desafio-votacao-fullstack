package ar.db.projeto_votacao.dto;

public record PautaResultadoDto(
        Long id,
        String titulo,
        Long totalVotos,
        Long totalVotosSim,
        Long totalVotosNao) {
}
