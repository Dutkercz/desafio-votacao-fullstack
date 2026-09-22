import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ResultPage } from "./ResultPage";
import { agendaService } from "@/services/agendaService";
import userEvent from "@testing-library/user-event";


vi.mock('@/services/agendaService', () => ({
    agendaService: {
        getResults: vi.fn(),
    },
}))

const mockNavigate = vi.fn()


vi.mock('react-router-dom', async (importOriginal) => {
    const actual = await importOriginal<typeof import('react-router-dom')>()
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    }
})
describe('ResultPage', () => {


    const renderWithProviders = (ui: React.ReactNode) => {
        const queryClient = new QueryClient({})

        return render(
            <QueryClientProvider client={queryClient}>
                {ui}
            </QueryClientProvider>
        )
    }

    beforeEach(() => {
        vi.resetAllMocks()
    })

    it('renders the list of results returned by the API', async () => {
        vi.mocked(agendaService.getResults).mockResolvedValue({
            content: [
                { id: 1, titulo: 'Pauta A', totalVotos: 10, totalVotosSim: 8, totalVotosNao: 2 },
                { id: 2, titulo: 'Pauta B', totalVotos: 10, totalVotosSim: 8, totalVotosNao: 2 }
            ],
            totalPages: 1,
            totalElements: 2,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: false,
        })
        renderWithProviders(<ResultPage />)

        expect(await screen.findByText('Resultados das votações')).toBeInTheDocument()
        expect(await screen.findByText('Pauta A')).toBeInTheDocument()
        expect(await screen.findByText('Pauta B')).toBeInTheDocument()
        expect(screen.getByRole('button', { name: 'Voltar a lista de Pautas' })).toBeInTheDocument()
        expect(screen.getAllByText('Total').length).toBeGreaterThan(0)
    })

    it('shows an empty state when there are no results', async () => {
        vi.mocked(agendaService.getResults).mockResolvedValue({
            content: [],
            totalPages: 1,
            totalElements: 0,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: true,
        })

        renderWithProviders(<ResultPage />)

        expect(await screen.findByText('Nenhum resultado disponível')).toBeInTheDocument()
        expect(screen.getByText('As pautas finalizadas aparecerão aqui.')).toBeInTheDocument()
    })

    it('navigates back to the home page when the button is clicked', async () => {
        const user = userEvent.setup()
        vi.mocked(agendaService.getResults).mockResolvedValue({
            content: [],
            totalPages: 1,
            totalElements: 0,
            size: 6,
            number: 0,
            first: true,
            last: true,
            empty: true,
        })

        renderWithProviders(<ResultPage />)
        const botaoVoltar = await screen.findByRole('button', { name: 'Voltar a lista de Pautas' })
        await user.click(botaoVoltar)
        expect(mockNavigate).toHaveBeenCalledWith('/')
    })
})
