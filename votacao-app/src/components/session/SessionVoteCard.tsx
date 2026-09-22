import { Countdown } from "@/components/countdown/Countdown"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { useState } from "react"
import { toast } from "sonner"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import type { AxiosError } from "axios"
import type { BackendError } from "@/types/backendError"
import type { SessionResponseDto } from "@/types/session"
import type { VoteRequest } from "@/types/vote"
import { onError } from "@/utils/onError"
import { voteService } from "../../services/voteService"

type SessionVoteCardProps = {
  session: SessionResponseDto
  agendaId: number
}

const SessionVoteCard = ({ session, agendaId }: SessionVoteCardProps) => {
  const [voteOption, setVoteOption] = useState("")
  const [associateId, setAssociateId] = useState<number>(1) //inicia com o usuario de ID 1
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
    <div key={session.id} className="flex items-center justify-between gap-3 rounded-md border border-border/80 bg-muted/30 px-3 py-2">
      <div>
        <p className="text-xs font-medium uppercase tracking-[0.12em] text-muted-foreground">
          Sessão em andamento
        </p>
        <span><Countdown session={session} /></span>

      </div>
      <div>
        <RadioGroup value={voteOption}
          onValueChange={setVoteOption} className="w-fit flex">
          <div className="flex items-center gap-3">
            <RadioGroupItem value="SIM" id="r1" />
            <Label htmlFor="r1">SIM</Label>
          </div>
          <div className="flex items-center gap-3">
            <RadioGroupItem value="NAO" id="r2" />
            <Label htmlFor="r2">NÃO</Label>
          </div>
        </RadioGroup>
      </div>
      <Button type="button"
        disabled={voteMutation.isPending || session?.status === "FINALIZADA"}
        onClick={submitVote} size="sm" className="min-w-26">
        {voteMutation.isPending ? "Votando" : "Votar"}
      </Button>
    </div>
  )
}

export default SessionVoteCard
