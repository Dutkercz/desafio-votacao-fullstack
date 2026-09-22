import type { SessionResponseDto } from "./session"

export type AgendaResult = {
    id: number
    titulo: string
    totalVotos: number
    totalVotosSim: number
    totalVotosNao: number
  }
  
  export type AgendaRequest = {
    titulo: string
  }
  
  export type AgendaResponse = {
    id: number,
    titulo: string
  }
  
  export type AgendaCardResponse = {
    id: number,
    titulo: string,
    sessao?: SessionResponseDto
    status: AgendaStatus
  }
  
  export type AgendaStatus = "NOVA" | "EM_VOTACAO" | "FINALIZADA"