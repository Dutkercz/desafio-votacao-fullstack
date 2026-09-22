import type { VoteRequest } from "@/types/vote"
import { apiClient } from "./apiClient"

export const voteService = {
    postVote: async (data: VoteRequest) => {
        const response = await apiClient.post("/votos", data)
        return response.data
    }
}
