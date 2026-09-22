import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import type { AgendaResult } from '@/types/agenda'
import { CalendarRange, CheckCircle2, XCircle } from 'lucide-react'

export type AgendaResultCardProps = {
  agenda: AgendaResult,
}

const AgendaResultCard = ({ agenda }: AgendaResultCardProps) => {
  return (
    <Card>
      <CardHeader className="bg-muted/40 px-4 py-4 sm:px-5">
        <div className="flex items-baseline gap-2">
          <div className="flex size-9 shrink-0 items-center justify-center rounded-md bg-primary/10 text-primary">
            <CalendarRange className="size-4" />
          </div>
          <CardTitle className="min-w-0 sm:text-lg">
            {agenda.titulo}
          </CardTitle>
        </div>
      </CardHeader>

      <CardContent className="grid grid-cols-3 divide-x px-0 py-0">
        <div className="flex flex-col gap-1 px-3 py-5 text-center sm:px-5">
          <span className="text-xs font-medium uppercase tracking-[0.12em] text-muted-foreground">
            Total
          </span>
          <span className="text-2xl font-semibold tabular-nums sm:text-3xl">
            {agenda.totalVotos}
          </span>
        </div>
        <div className="flex flex-col gap-1 px-3 py-5 text-center sm:px-5">
          <span className="inline-flex items-center justify-center gap-1 text-xs font-medium uppercase tracking-[0.12em] text-emerald-700 dark:text-emerald-400">
            <CheckCircle2 className="size-3.5" aria-hidden="true" />
            Sim
          </span>
          <span className="text-2xl font-semibold tabular-nums text-emerald-700 dark:text-emerald-400 sm:text-3xl">
            {agenda.totalVotosSim}
          </span>
        </div>
        <div className="flex flex-col gap-1 px-3 py-5 text-center sm:px-5">
          <span className="inline-flex items-center justify-center gap-1 text-xs font-medium uppercase tracking-[0.12em] text-destructive">
            <XCircle className="size-3.5" aria-hidden="true" />
            Não
          </span>
          <span className="text-2xl font-semibold tabular-nums text-destructive sm:text-3xl">
            {agenda.totalVotosNao}
          </span>
        </div>
      </CardContent>
    </Card>
  )
}

export default AgendaResultCard
