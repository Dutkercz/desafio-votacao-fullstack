import type { AgendaCardResponse, AgendaRequest, AgendaResult } from "@/types/agenda"
import { apiClient } from "./apiClient"
import type { DefaultPageResponse } from "../types/pageResponse"

export const agendaService = {
  getAllNotVoted: async (page: number) => {
    const response = await apiClient.get<DefaultPageResponse<AgendaCardResponse>>(`/pautas?size=6&page=${page}`)
    return response.data
  },
  addAgenda: async (data: AgendaRequest) => {
    const response = await apiClient.post("/pautas", data)
    return response.data
  },
  getResults: async (page: number) => {
    const response = await apiClient.get<DefaultPageResponse<AgendaResult>>(`/pautas/resultados?size=6&page=${page}`)
    return response.data
  }
}
