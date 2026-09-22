import type { AssociateRequest } from "@/types/associate"
import { apiClient } from "./apiClient"

export const associateService = {
    registerAssociate: async (data: AssociateRequest) => {
        const response = await apiClient.post("/associados", data)
        return response.data
    }

}
