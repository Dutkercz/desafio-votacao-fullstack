import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import AgendaResultCard from "./AgendaResultCard";
import type { AgendaResult } from "@/types/agenda";

describe('AgendaResultCard', () => {

    const mockAgenda: AgendaResult = {
        id: 7,
        titulo: 'Aprovar compra de mesas',
        totalVotos: 15,
        totalVotosSim: 10,
        totalVotosNao: 5,
    }


    const renderAgenda = () =>
        render(
            <AgendaResultCard
                agenda={mockAgenda}
            />
        )
        
    beforeEach(() => {
        vi.clearAllMocks()
    })

    it('renders title and vote totals for the agenda', () => {

        renderAgenda()

        expect(screen.getByText('Aprovar compra de mesas')).toBeInTheDocument()
        expect(screen.getByText('Total')).toBeInTheDocument()
        expect(screen.getByText('15')).toBeInTheDocument()
        expect(screen.getByText('Sim')).toBeInTheDocument()
        expect(screen.getByText('10')).toBeInTheDocument()
        expect(screen.getByText('Não')).toBeInTheDocument()
        expect(screen.getByText('5')).toBeInTheDocument()
    })
})
