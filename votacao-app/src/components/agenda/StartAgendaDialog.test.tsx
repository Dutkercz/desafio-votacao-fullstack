import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { useAgendaDialog } from '@/hooks/useAgendaDialog'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { Dialog } from '../ui/dialog'
import StartAgendaDialog from './StartAgendaDialog'

vi.mock('@/hooks/useAgendaDialog', () => ({
  useAgendaDialog: vi.fn(),
}))

describe('StartAgendaDialog', () => {
  const renderDialog = () =>
          render (
              <Dialog open={true} onOpenChange={vi.fn()}>
                  <StartAgendaDialog setOpen={vi.fn()} />
              </Dialog>
          )

  const mockSubmitAgenda = vi.fn()
  const mockSetTitle = vi.fn()

  const mockHookReturn = {
    isPending : false, 
    submitAgenda :mockSubmitAgenda, 
    title: "Nova pauta", 
    setTitle: mockSetTitle
  }
  
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('shows the agenda title input and submits the form', async () => {
    const user = userEvent.setup()
    vi.mocked(useAgendaDialog).mockReturnValue(mockHookReturn)

    renderDialog()

    const input = screen.getByLabelText('Titulo da Pauta')
    expect(input).toHaveValue('Nova pauta')

    await user.clear(input)
    await user.type(input, 'Nova pauta do teste')
    await waitFor(() => {
      expect(mockSetTitle).toHaveBeenCalled()
    })
    await user.click(screen.getByRole('button', { name: 'Adicionar' }))
    expect(mockSubmitAgenda).toHaveBeenCalledTimes(1)
  })

  it('disables the submit button while the mutation is pending', () => {
    vi.mocked(useAgendaDialog).mockReturnValue({...mockHookReturn, isPending : true})
    renderDialog()

    expect(screen.getByRole('button', { name: 'Adicionar' })).toBeDisabled()
  })
})
