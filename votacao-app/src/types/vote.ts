export type VoteRequest = {
    associadoId: number,
    pautaId: number,
    tipoVoto: VoteOption
}

export type VoteOption = "SIM" | "NAO"
