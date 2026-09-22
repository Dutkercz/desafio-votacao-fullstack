import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { Dialog } from '@/components/ui/dialog'
import { RegisterAssociateDialog } from './RegisterAssociateDialog'
import { associateService } from '@/services/associateService'

vi.mock('@/services/associateService', () => ({
    associateService: {
        registerAssociate: vi.fn(),
    },
}))

describe('RegisterAssociateDialog', () => {
    
    const renderDialog = (setOpen = vi.fn()) => {
        const queryClient = new QueryClient({})
        
        return render(
            <QueryClientProvider client={queryClient}>
                <Dialog open={true} onOpenChange={vi.fn()}>
                    <RegisterAssociateDialog setOpen={setOpen} />
                </Dialog>
            </QueryClientProvider>
        )
    }
    
    beforeEach(() => {
        vi.clearAllMocks()
    })

    it('renders CPF field and submits a valid associate', async () => {
        const user = userEvent.setup()
        const setOpen = vi.fn()

        vi.mocked(associateService.registerAssociate).mockResolvedValue({ success: true })

        renderDialog(setOpen)

        const input = screen.getByLabelText('CPF')
        const submitButton = screen.getByRole('button', { name: 'Registrar' })

        expect(input).toBeInTheDocument()
        expect(submitButton).toBeInTheDocument()

        await user.type(input, '12345678909')
        expect(input).toHaveValue('123.456.789-09')

        await user.click(submitButton)
        await waitFor(() => {
            expect(associateService.registerAssociate).toHaveBeenCalledWith({ cpf: '12345678909' })
        })

        await waitFor(() => {
            expect(setOpen).toHaveBeenCalledWith(false)
        })
    })

    it('prevents submission when CPF is empty or invalid', async () => {
        const user = userEvent.setup()
        vi.mocked(associateService.registerAssociate).mockResolvedValue({ success: true })

        renderDialog()

        await user.click(screen.getByRole('button', { name: 'Registrar' }))

        expect(associateService.registerAssociate).not.toHaveBeenCalled()
        expect(await screen.findByText('CPF fora do formato esperado')).toBeInTheDocument()
    })
})
