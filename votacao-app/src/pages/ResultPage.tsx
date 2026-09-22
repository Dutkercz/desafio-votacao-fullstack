import { agendaService } from "@/services/agendaService"
import AgendaResultCard from "@/components/agenda/AgendaResultCard"
import PaginationCard from "@/components/pagination/PaginationCard"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { useQuery } from "@tanstack/react-query"
import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { useNavigate } from "react-router-dom"

export const ResultPage = () => {
  const [page, setPage] = useState(0)
  const navigate = useNavigate()

  const { data } = useQuery({
    queryKey: ["agendas", page],
    queryFn: () => agendaService.getResults(page)
  })
  const totalPages = data?.totalPages ?? 1

  const handleHomeClick = () => {
    navigate("/")
  }

  return (
    <div className="min-h-screen p-2 sm:p-4 md:p-6 flex justify-center">
      <Card className="w-full  border rounded-lg">
        <CardHeader className="px-4 py-6 sm:px-6 sm:py-8">
          <CardTitle className="text-center text-2xl sm:text-3xl md:text-4xl">
            Resultados das votações
          </CardTitle>
          <CardDescription className="text-center text-base sm:text-lg">
            Confira como os associados votaram em cada pauta
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div>
            <Button variant="outline" onClick={handleHomeClick}>
              Voltar a lista de Pautas
            </Button>
          </div>
          <Separator orientation="horizontal" className="mb-2 mt-2" />

          {data?.content && data.content.length > 0 ? (
            <div className='grid grid-cols-1 gap-2 sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-2'>
              {data.content.map((agenda) => (
                <AgendaResultCard key={agenda.id} agenda={agenda} />
              ))}
            </div>
          ) : (
            <div className="rounded-lg border border-dashed bg-muted/30 px-6 py-12 text-center">
              <p className="font-medium">Nenhum resultado disponível</p>
              <p className="mt-1 text-sm text-muted-foreground">
                As pautas finalizadas aparecerão aqui.
              </p>
            </div>
          )}
        </CardContent>
        <CardFooter>
          <PaginationCard
            currentPage={page}
            onPageChange={(p) => setPage(p)}
            totalPages={totalPages} />
        </CardFooter>
      </Card>
    </div>
  )
}
