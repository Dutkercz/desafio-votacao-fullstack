import { Countdown } from "@/components/countdown/Countdown"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { useState, type Dispatch, type SetStateAction } from "react"
import { toast } from "sonner"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import type { AxiosError } from "axios"
import type { BackendError } from "@/types/backendError"
import type { SessionResponseDto } from "@/types/session"
import type { VoteRequest } from "@/types/vote"
import { onError } from "@/utils/onError"
import { voteService } from "../../services/voteService"
import { Card, CardContent } from "../ui/card"

type SessionVoteCardProps = {
  session: SessionResponseDto
  agendaId: number
  associateId: number
  setAssociateId: Dispatch<SetStateAction<number>>
}

const SessionVoteCard = ({ session, agendaId, associateId, setAssociateId }: SessionVoteCardProps) => {
  const [voteOption, setVoteOption] = useState("")
  const queryClient = useQueryClient()

  const voteMutation = useMutation({
    mutationFn: (data: VoteRequest) => voteService.postVote(data),
    onSuccess: () => {
      toast.success("Voto realizado com sucesso")
      setVoteOption("")
      //aqui é simulado o voto de um novo associado, 
      //aumenta em 1 a cada voto pra não por o id manualmente, se fosse alguem autenticado
      //pegaria o ID do usuario
      setAssociateId(() => associateId + 1)
      queryClient.invalidateQueries({ queryKey: ["agendas"] })
      queryClient.invalidateQueries({ queryKey: ["agendas-result"] })
    },
    onError: (error: AxiosError<BackendError>) => {
      onError(error)
    }
  })

  const submitVote = () => {
    if (!voteOption || (voteOption !== "SIM" && voteOption !== "NAO")) {
      toast.error("Por favor, vote em SIM ou NÂO")
      return
    }
    voteMutation.mutate({ associadoId: associateId, pautaId: agendaId, tipoVoto: voteOption })
  }

  return (
    <Card key={session.id} className="rounded-md border border-border/80 bg-muted/30">
      <CardContent className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div className="min-w-0">
          <p className="text-xs font-medium uppercase tracking-[0.12em] text-muted-foreground">
            Sessão em andamento
          </p>
          <span><Countdown session={session} /></span>

        </div>
        <div className="flex w-full flex-col gap-3 sm:w-auto sm:flex-row sm:items-center">
          <RadioGroup value={voteOption}
            onValueChange={setVoteOption} className="flex w-full justify-between sm:w-fit sm:justify-start">
            <div className="flex items-center gap-3">
              <RadioGroupItem value="SIM" id="r1" />
              <Label htmlFor="r1">SIM</Label>
            </div>
            <div className="flex items-center gap-3">
              <RadioGroupItem value="NAO" id="r2" />
              <Label htmlFor="r2">NÃO</Label>
            </div>
          </RadioGroup>
          <Button type="button"
            disabled={voteMutation.isPending || session?.status === "FINALIZADA"}
            onClick={submitVote} size="sm" className="w-full sm:w-auto sm:min-w-26">
            {voteMutation.isPending ? "Votando" : "Votar"}
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}

export default SessionVoteCard
