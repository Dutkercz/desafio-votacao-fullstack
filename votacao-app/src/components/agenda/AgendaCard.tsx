import { Card, CardContent, CardHeader, CardTitle } from '../ui/card'
import SessionVoteCard from '@/components/session/SessionVoteCard'
import SessionStartCard from '@/components/session/SessionStartCard'
import type { AgendaCardResponse } from '@/types/agenda'
import { CalendarRange } from 'lucide-react'

type AgendaCardProps = {
  agenda: AgendaCardResponse
}

const AgendaCard = ({ agenda }: AgendaCardProps) => {
  const session = agenda.sessao

  const stylesMap: Record<string, string> = {
    NOVA: "border-emerald-500/30 bg-emerald-500/10 text-emerald-700 dark:text-emerald-400",
    EM_VOTACAO: "border-amber-500/30 bg-amber-500/10 text-amber-700 dark:text-amber-400",
    FINALIZADA: "border-destructive/30 bg-destructive/10 text-destructive dark:text-destructive"
  }

  return (
    <Card className="bg-card shadow-sm">
      <CardHeader className="bg-muted/40 px-4 py-4 sm:px-5">
        <div className="flex items-start justify-between gap-3">
          <div className="flex items-baseline gap-2">
            <div className="flex size-9 shrink-0 items-center justify-center rounded-md bg-primary/10 text-primary">
              <CalendarRange className="size-4" />
            </div>
            <CardTitle className="min-w-0 sm:text-lg">
              {agenda.titulo}
            </CardTitle>
          </div>

          <span
            className={`inline-flex items-center rounded-full border px-2.5 py-1 text-[10px] font-medium uppercase tracking-[0.12em] 
              ${stylesMap[agenda.status]}`}
          >
            {agenda.status === "NOVA" ? "Nova" : "Em Votação"}
          </span>
        </div>
      </CardHeader>

      <CardContent className="px-4 py-4 sm:px-5">
        {session?.status === "EM_ANDAMENTO" ? (
          /*Primeiro card é de sessão já em votação */
          <SessionVoteCard agendaId={agenda.id} session={session} />
        )
          :
          (
            /* segundo Card é para iniciar a sessão de votação */
            <SessionStartCard agendaId={agenda.id} />
          )
        }
      </CardContent>
    </Card>
  )
}

export default AgendaCard
