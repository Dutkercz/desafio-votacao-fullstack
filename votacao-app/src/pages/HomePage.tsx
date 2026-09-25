import { agendaService } from "@/services/agendaService"
import AgendaCard from "@/components/agenda/AgendaCard"
import PaginationCard from "@/components/pagination/PaginationCard"
import StartAgendaDialog from "@/components/agenda/StartAgendaDialog"
import { RegisterAssociateDialog } from "@/components/register/RegisterAssociateDialog"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogTrigger } from "@/components/ui/dialog"
import { useQuery } from "@tanstack/react-query"
import { useState, type Dispatch, type SetStateAction } from "react"
import { useNavigate } from "react-router-dom"
import type { AgendaCardResponse } from "@/types/agenda"
import { Separator } from "@/components/ui/separator"

type HomePagePrps = {
  associateId : number
  setAssociateId: Dispatch<SetStateAction<number>>
}

const HomePage = ({associateId, setAssociateId} : HomePagePrps) => {
  const [openPauta, setOpenPauta] = useState(false)
  const [openCadastro, setOpenCadastro] = useState(false)
  const [page, setPage] = useState(0)
  const navigate = useNavigate()

  const handleClickResult = () => {
    navigate("/results")
  }

  const { data: agendas } = useQuery({
    queryKey: ["agendas", page],
    queryFn: () => agendaService.getAllNotVoted(page),
  })

  const agendaContent = agendas?.content
  const totalPages = agendas?.totalPages ?? 1

  return (
    <div className="min-h-screen p-2 sm:p-4 md:p-6 flex justify-center">
      <Card className="w-full  border rounded-lg">
        <CardHeader className="px-4 py-6 sm:px-6 sm:py-8">
          <CardTitle className="text-center text-2xl sm:text-3xl md:text-4xl">
            Sistema de votação
          </CardTitle>
          <CardDescription className="text-center text-lg">
            Acompanhe as pautas em andamento
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="mb-2 grid grid-cols-1 gap-2 sm:grid-cols-3">
            <div>
              <Dialog open={openPauta} onOpenChange={setOpenPauta}>
                <DialogTrigger render={
                  <Button className="w-full" variant="outline" onClick={(v) => setOpenPauta(!v)}>
                    Adicionar Pauta
                  </Button>}
                />
                <StartAgendaDialog setOpen={setOpenPauta} />
              </Dialog>
            </div>
            <div>
              <Dialog open={openCadastro} onOpenChange={setOpenCadastro}>
                <DialogTrigger render={
                  <Button className="w-full" variant="outline" onClick={(v) => setOpenCadastro(!v)}>
                    Cadastrar Associado
                  </Button>} />
                <RegisterAssociateDialog setOpen={setOpenCadastro} />
              </Dialog>
            </div>
            <div>
              <Button className="w-full" variant="outline" onClick={handleClickResult}>
                Resultados
              </Button>
            </div>
          </div>
          <Separator orientation="horizontal" className="mb-2" />
          {agendaContent && agendaContent.length > 0 ?
            (
              <div
                className='grid grid-cols-1 gap-2 sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-2'>
                {agendaContent.map((agenda: AgendaCardResponse) => (
                  <AgendaCard associateId={associateId} setAssociateId={setAssociateId}
                    key={agenda.id} agenda={agenda} />
                ))}
              </div>
            ) : (
              <div>
                <Card>
                  <CardTitle className="m-1 text-center">
                    Não existem pautas a serem votadas
                  </CardTitle>
                </Card>
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
    </div >
  )
}

export default HomePage