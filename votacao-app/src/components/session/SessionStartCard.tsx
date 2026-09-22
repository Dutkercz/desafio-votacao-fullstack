import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import type { SessionRequest } from '@/types/session'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import type { AxiosError } from 'axios'
import { useState, type ChangeEvent } from 'react'
import { toast } from 'sonner'
import { sessionService } from '../../services/sessionService'
import type { BackendError } from '@/types/backendError'
import { onError } from '@/utils/onError'
import { Card, CardContent } from '../ui/card'

type SessionStartCardProps = {
  agendaId: number
}

const SessionStartCard = ({ agendaId }: SessionStartCardProps) => {
  const [duration, setDuration] = useState(1)
  const queryClient = useQueryClient()

  const handleDurationChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = Number(event.target.value)
    setDuration(Number.isNaN(value) ? 0 : value)
  }

  const openSessionMutate = useMutation({
    mutationFn: (data: SessionRequest) => sessionService.newSession(data),
    onSuccess: () => {
      toast.success("Sessão iniciada com sucesso!")
      queryClient.invalidateQueries({ queryKey: ["agendas"] })
    },
    onError: (error: AxiosError<BackendError>) => {
      onError(error)
    }
  })

  const submitSession = () => {
    openSessionMutate.mutate({ pautaId: agendaId, duracao: duration })
  }
  return (
    <Card className='rounded-md border border-border/80 bg-muted/30'>
      <CardContent className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
        <div className="w-full sm:w-auto">
          <Label htmlFor="duration" className="text-xs font-medium uppercase tracking-[0.12em] text-muted-foreground">
            Duração da sessão
          </Label>
          <Input
            id="duration"
            name="duration"
            type="number"
            min={1}
            value={duration}
            onChange={handleDurationChange}
            className="h-10 w-full min-w-0 sm:w-24"
          />
        </div>

        <Button
          type="button"
          onClick={submitSession}
          disabled={duration < 1}
          className="w-full sm:w-auto"
        >
          Iniciar Sessão
        </Button>
      </CardContent>
    </Card>
  )
}

export default SessionStartCard
