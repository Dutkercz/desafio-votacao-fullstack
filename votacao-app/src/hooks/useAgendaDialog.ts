import { agendaService } from "@/services/agendaService"
import type { BackendError } from "@/types/backendError"
import { onError } from "@/utils/onError"
import { useQueryClient, useMutation } from "@tanstack/react-query"
import type { AxiosError } from "axios"
import { useState } from "react"

export const useAgendaDialog = ({ setOpen }: { setOpen: (v: boolean) => void }) => {
  const [title, setTitle] = useState("")
  const queryClient = useQueryClient()

  const addAgendaMutation = useMutation({
    mutationFn: () => agendaService.addAgenda({ titulo: title }),
    onSuccess: () => {
      setTitle("")
      setOpen(false)
      queryClient.invalidateQueries({ queryKey: ["agendas"] })
    },
    onError: (error: AxiosError<BackendError>) => {
      onError(error)
    }
  })

  const submitAgenda = () => {
    addAgendaMutation.mutate()
  }

  const isPending = addAgendaMutation.isPending

  return { submitAgenda, isPending, title, setTitle }
}