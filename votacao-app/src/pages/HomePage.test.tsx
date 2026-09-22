import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import HomePage from "./HomePage";
import { agendaService } from "@/services/agendaService";
import userEvent from "@testing-library/user-event";

const mockNavigate = vi.fn()

vi.mock("react-router-dom", async (original) => {
    const actual = await original<typeof import('react-router-dom')>()
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    }
})

vi.mock('@/services/agendaService', () => ({
    agendaService: {
        getAllNotVoted: vi.fn(),
    },
}))

describe("HomePage test component", () => {

    const renderWithProviders = (ui: React.ReactNode) => {
        const queryClient = new QueryClient({})

        return render(
            <QueryClientProvider client={queryClient}>
                {ui}
            </QueryClientProvider>
        )
    }

    it("Should render infos and functionalities from HomePage when a agenda is in voting", async () => {
        vi.mocked(agendaService.getAllNotVoted).mockResolvedValue({
            content: [
                {
                    id: 1, titulo: 'Pauta A', status: "EM_VOTACAO", sessao: {
                        id: 1,
                        inicio: "2026-09-20T20:00:00",
                        fim: "2026-09-20T20:05:00",
                        status: "EM_ANDAMENTO"
                    }
                }
            ],
            totalPages: 1,
            totalElements: 1,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: false,
        })
        renderWithProviders(<HomePage />)

        expect(await screen.findByText("Acompanhe as pautas em andamento")).toBeInTheDocument()
        expect(await screen.findByText("Sistema de votação")).toBeInTheDocument()
        expect(await screen.findByText("Pauta A")).toBeInTheDocument()
        expect(await screen.findByText("Em Votação")).toBeInTheDocument()
        expect(await screen.getByRole("button", {name: "Adicionar Pauta"})).toBeInTheDocument()
        expect(await screen.getByRole("button", {name: "Cadastrar Associado"})).toBeInTheDocument()
        expect(await screen.getByRole("button", {name: "Resultados"})).toBeInTheDocument()
    })

    it("Should render infos and functionalities from HomePage have no one agenda info", async () => {
        vi.mocked(agendaService.getAllNotVoted).mockResolvedValue({
            content: [],
            totalPages: 0,
            totalElements: 0,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: false,
        })
        renderWithProviders(<HomePage />)

        expect(await screen.findByText("Acompanhe as pautas em andamento")).toBeInTheDocument()
        expect(await screen.findByText("Sistema de votação")).toBeInTheDocument()
        expect(await screen.findByText("Não existem pautas a serem votadas")).toBeInTheDocument() 
    })

    it("Should render infos and functionalities from HomePage when a agenda have not started a session", async () => {
        vi.mocked(agendaService.getAllNotVoted).mockResolvedValue({
            content: [
                {
                    id: 1, 
                    titulo: 'Pauta A', 
                    status: "NOVA", 
                    sessao: undefined
                }
            ],
            totalPages: 1,
            totalElements: 1,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: false,
        })
        renderWithProviders(<HomePage />)

        expect(await screen.findByText("Acompanhe as pautas em andamento")).toBeInTheDocument()
        expect(await screen.findByText("Sistema de votação")).toBeInTheDocument()
        expect(await screen.findByText("Nova")).toBeInTheDocument()
    })

    it("Should open add Agenda dialog", async () => {
        renderWithProviders(<HomePage />)
        
        const user = userEvent.setup()

        const addAgendaButton = screen.getByRole("button", {name: "Adicionar Pauta"})
        expect(addAgendaButton).toBeInTheDocument()
        await user.click(addAgendaButton)

        expect(screen.getByText("Titulo da Pauta")).toBeInTheDocument()
        expect(screen.getByRole("button", {name: "Adicionar"})).toBeInTheDocument()
    })
})
