import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { render, screen } from "@testing-library/react"
import userEvent from "@testing-library/user-event"
import { beforeEach, describe, expect, it, vi } from "vitest"
import { sessionService } from "@/services/sessionService"
import SessionStartCard from "./SessionStartCard"

vi.mock("@/services/sessionService", () => ({
    sessionService: {
        newSession: vi.fn(),
    },
}))

describe("SessionStartCard", () => {
    const renderCard = () => {
        const queryClient = new QueryClient()

        return render(
            <QueryClientProvider client={queryClient}>
                <SessionStartCard agendaId={7} />
            </QueryClientProvider>,
        )
    }

    beforeEach(() => {
        vi.clearAllMocks()
        vi.mocked(sessionService.newSession).mockResolvedValue({})
    })

    it("renders the duration field and start button", () => {
        renderCard()

        expect(screen.getByLabelText("Duração da sessão")).toHaveValue(1)
        expect(screen.getByRole("button", { name: "Iniciar Sessão" })).toBeEnabled()
    })

    it("submits the agenda id and selected duration", async () => {
        const user = userEvent.setup()
        renderCard()

        const durationInput = screen.getByLabelText("Duração da sessão")
        await user.clear(durationInput)
        await user.type(durationInput, "15")
        await user.click(screen.getByRole("button", { name: "Iniciar Sessão" }))

        await vi.waitFor(() => {
            expect(sessionService.newSession).toHaveBeenCalledWith({ pautaId: 7, duracao: 15 })
        })
    })

    it("disables starting when the duration is invalid", async () => {
        const user = userEvent.setup()
        renderCard()

        const durationInput = screen.getByLabelText("Duração da sessão")
        await user.clear(durationInput)

        expect(screen.getByRole("button", { name: "Iniciar Sessão" })).toBeDisabled()
    })
})
