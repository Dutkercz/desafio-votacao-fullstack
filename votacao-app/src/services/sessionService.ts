import type { SessionRequest } from "@/types/session"
import { apiClient } from "./apiClient"

export const sessionService = {
    newSession: async (data: SessionRequest) => {
        const response = await apiClient.post("/sessoes", data)
        return response.data
    },
    endSession: async (id: number) => {
        const response = await apiClient.patch(`/sessoes/${id}`)
        return response.data
    }
}
