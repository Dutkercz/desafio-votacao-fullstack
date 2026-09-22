import { render, screen, waitFor } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { Countdown } from './Countdown'
import { useCountdown } from '@/hooks/useCountdown'

vi.mock('@/hooks/useCountdown', () => ({
    useCountdown: vi.fn(),
}))

describe('CountDownTime', () => {
    const session = {
        id: 42,
        inicio: '2024-01-01T00:00:00.000Z',
        fim: '2024-01-01T00:10:00.000Z',
        status: 'EM_ANDAMENTO' as const,
    }

    const handleSubmit = vi.fn()

    const mockReturnCountdown = {
        minutes: 1,
        seconds: '1',
        countdown: 150,
        handleSubmit: handleSubmit,
        isPending: false
    }

    beforeEach(() => {
        vi.clearAllMocks()
    })

    it('renders remaining time while the session is active', () => {
        vi.mocked(useCountdown).mockReturnValue(mockReturnCountdown)

        render(<Countdown session={session} />)

        expect(screen.getByText('Tempo restante')).toBeInTheDocument()
        expect(screen.getByText('1:1')).toBeInTheDocument()
    })

    it('calls endSessionMutation when countdown reaches zero during an active session', async () => {
        vi.mocked(useCountdown).mockReturnValue({...mockReturnCountdown, countdown: 0})

        render(<Countdown session={session} />)
        await waitFor(() => {

            expect(handleSubmit).toHaveBeenCalledWith(42)
        })
    })

    it('renders the session as finished when it is no longer active', () => {
        vi.mocked(useCountdown).mockReturnValue({...mockReturnCountdown, countdown: 0})

        render(
            <Countdown
                session={{
                    ...session,
                    status: 'FINALIZADA',
                }}
            />
        )

        expect(screen.getByText('Votação finalizada')).toBeInTheDocument()
        expect(screen.queryByText('Tempo restante')).not.toBeInTheDocument()
    })
})
