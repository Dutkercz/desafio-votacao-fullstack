import type { AgendaCardResponse } from "@/types/agenda"
import { render, screen } from "@testing-library/react"
import { beforeEach, describe, expect, it, vi } from "vitest"
import AgendaCard from "./AgendaCard"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"

describe("AgendaCard", () => {

    const mockAgenda: AgendaCardResponse = {
        id: 1,
        titulo: "Titulo",
        sessao: {
            id: 1,
            inicio: "2026-09-20T12:00:00",
            fim: "2026-09-20T12:05:00",
            status: "EM_ANDAMENTO",
        },
        status: "EM_VOTACAO"
    }

    const renderWithProviders = (ui: React.ReactElement) => {
        const queryClient = new QueryClient({});

        return render(
            <QueryClientProvider client={queryClient}>
                {ui}
            </QueryClientProvider>
        );
    };

    beforeEach(() => {
        vi.clearAllMocks()
    })

    it("Should render all infos from Agenda and Session infos when it is voting", () => {
        renderWithProviders(<AgendaCard agenda={mockAgenda} />)

        expect(screen.getByText('Titulo')).toBeInTheDocument()
        expect(screen.getByText('Em Votação')).toBeInTheDocument()
        expect(screen.getByText('Sessão em andamento')).toBeInTheDocument()
    })

    it("Should render all infos from Agenda and Session infos when voting not started", () => {
        renderWithProviders(<AgendaCard agenda={{...mockAgenda, status: "NOVA", sessao: undefined }} />)

        expect(screen.getByText('Titulo')).toBeInTheDocument()
        expect(screen.getByText('Nova')).toBeInTheDocument()
        expect(screen.getByText('Duração da sessão')).toBeInTheDocument()
    })

    it("Should render all infos from Agenda and Session infos when voting not started", () => {
        renderWithProviders(<AgendaCard agenda={{...mockAgenda, status: "NOVA", sessao: undefined }} />)

        expect(screen.getByText('Titulo')).toBeInTheDocument()
        expect(screen.getByText('Nova')).toBeInTheDocument()
        expect(screen.getByText('Duração da sessão')).toBeInTheDocument()
    })

})