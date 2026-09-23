import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { render, screen } from "@testing-library/react"
import userEvent from "@testing-library/user-event"
import { afterAll, beforeAll, beforeEach, describe, expect, it, vi } from "vitest"
import { voteService } from "@/services/voteService"
import type { SessionResponseDto } from "@/types/session"
import SessionVoteCard from "./SessionVoteCard"

vi.mock("@/services/voteService", () => ({
    voteService: {
        postVote: vi.fn(),
    },
}))

vi.mock("@/components/countdown/Countdown", () => ({
    Countdown: () => null,
}))

describe("SessionVoteCard", () => {
    const session: SessionResponseDto = {
        id: 3,
        inicio: "2026-09-23T12:00:00",
        fim: "2026-09-23T12:05:00",
        status: "EM_ANDAMENTO",
    }

    const renderCard = (currentSession = session) => {
        const queryClient = new QueryClient()

        return render(
            <QueryClientProvider client={queryClient}>
                <SessionVoteCard session={currentSession} agendaId={9} />
            </QueryClientProvider>,
        )
    }

    beforeEach(() => {
        vi.clearAllMocks()
        vi.mocked(voteService.postVote).mockResolvedValue({})
    })

    beforeAll(() => {
        Object.defineProperty(window, "PointerEvent", {
            configurable: true,
            value: MouseEvent,
        })
    })

    afterAll(() => {
        Reflect.deleteProperty(window, "PointerEvent")
    })

    it("renders the voting options and button", () => {
        renderCard()

        expect(screen.getByRole("radio", { name: "SIM" })).toBeInTheDocument()
        expect(screen.getByRole("radio", { name: "NÃO" })).toBeInTheDocument()
        expect(screen.getByRole("button", { name: "Votar" })).toBeEnabled()
    })

    it("submits the selected vote with the agenda and associate ids", async () => {
        const user = userEvent.setup()
        renderCard()

        await user.click(screen.getByRole("radio", { name: "SIM" }))
        await user.click(screen.getByRole("button", { name: "Votar" }))

        await vi.waitFor(() => {
            expect(voteService.postVote).toHaveBeenCalledWith({
                associadoId: 1,
                pautaId: 9,
                tipoVoto: "SIM",
            })
        })
    })

    it("disables voting after the session is finalized", () => {
        renderCard({ ...session, status: "FINALIZADA" })

        expect(screen.getByRole("button", { name: "Votar" })).toBeDisabled()
    })
})
