export type SessionRequest = {
    pautaId: number
    duracao?: number
}

export type SessionResponseDto = {
    id: number,
    inicio: string,
    fim: string,
    status: SessionStatus
}

export type SessionStatus = "EM_ANDAMENTO" | "FINALIZADA"